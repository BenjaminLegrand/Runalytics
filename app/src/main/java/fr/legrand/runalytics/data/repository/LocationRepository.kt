package fr.legrand.runalytics.data.repository

import android.location.Location
import fr.legrand.runalytics.data.component.LogComponent
import fr.legrand.runalytics.data.manager.location.LocationManager
import fr.legrand.runalytics.data.manager.storage.StorageManager
import fr.legrand.runalytics.data.mapper.SessionDBEntityDataMapper
import fr.legrand.runalytics.data.model.RALocation
import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.data.values.LocationValues
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

private const val M_TO_KM = 1000
private const val LOW_SPEED_THRESHOLD = 1.5
private const val SESSION_TIMER_STEP = 1L //in sec

class LocationRepository(
    private val locationManager: LocationManager,
    private val storageManager: StorageManager,
    private val logComponent: LogComponent,
    private val sessionDBEntityDataMapper: SessionDBEntityDataMapper
) {

    private var currentRALocation = RALocation()
    private var currentSession = Session()

    fun startLocationComputation(): Observable<RALocation> = Observable.defer {
        currentSession.id = System.currentTimeMillis()
        currentSession.startDate = System.currentTimeMillis()
        logComponent.startSession(currentSession.id)
        var lastLocation: Location? = null
        logComponent.i("New session : $currentSession")
        return@defer locationManager.requestLocationUpdates().timeInterval()
            .map {
                val location = it.value()
                val distance = if (lastLocation == null) 0f else location.distanceTo(lastLocation)
                val fullDistance =
                    if (lastLocation == null) 0f else currentRALocation.fullDistance + distance
                val currentSpeed =
                    if (lastLocation == null) 0f else distance / it.time(TimeUnit.SECONDS)
                val currentKmTime =
                    if (lastLocation == null || distance == 0f) LocationValues.NO_TIME else (it.time(
                        TimeUnit.SECONDS
                    ) * M_TO_KM / distance).toInt()
                val timestamp =
                    if (lastLocation == null) 0L else currentRALocation.timestamp + it.time()
                val fullAltitudeDiff = if (lastLocation == null) 0f else
                    currentRALocation.fullAltitudeDiff + location.altitude.toFloat() - currentRALocation.altitude
                lastLocation = location
                currentRALocation = currentRALocation.copy(
                    currentSpeed = currentSpeed,
                    lastDistance = distance,
                    fullDistance = fullDistance,
                    altitude = location.altitude.toFloat(),
                    fullAltitudeDiff = fullAltitudeDiff,
                    timestamp = timestamp,
                    currentKmTime = currentKmTime
                )
                logComponent.i("New location computed : $currentRALocation")
                currentRALocation
            }.filter {
                it.currentSpeed > LOW_SPEED_THRESHOLD
            }.doOnNext {
                logComponent.i("New location added : $it")
                currentSession.locations.add(it)
            }
    }.doOnError {
        logComponent.e(it.message)
    }

    fun stopLocationComputation() = Completable.defer {
        currentSession.endDate = System.currentTimeMillis()
        logComponent.i("Ending session : $currentSession")
        logComponent.endSession(currentSession.id)
        locationManager.stopLocationUpdates()
        Completable.complete()
    }

    fun saveCurrentSession(): Completable = Completable.defer {
        logComponent.i("Saving session : $currentSession")
        storageManager.saveSession(sessionDBEntityDataMapper.transform(currentSession))
        currentSession = Session()
        currentRALocation = RALocation()
        Completable.complete()
    }

    fun getAllSessions(): Single<List<Session>> = Single.defer {
        Single.just(sessionDBEntityDataMapper.transformEntity(storageManager.getAllSessions()).filter { it.locations.isNotEmpty() })
    }

    fun startSessionTimer(): Observable<Long> =
        Observable.interval(SESSION_TIMER_STEP, TimeUnit.SECONDS)

}