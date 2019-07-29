package fr.legrand.runalytics.data.repository

import android.location.Location
import fr.legrand.runalytics.data.manager.location.LocationManager
import fr.legrand.runalytics.data.manager.storage.StorageManager
import fr.legrand.runalytics.data.mapper.SessionDBEntityDataMapper
import fr.legrand.runalytics.data.model.RALocation
import fr.legrand.runalytics.data.model.Session
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val M_TO_KM = 1000
private const val LOW_SPEED_THRESHOLD = 1.5

class LocationRepository(
    private val locationManager: LocationManager,
    private val storageManager: StorageManager,
    private val sessionDBEntityDataMapper: SessionDBEntityDataMapper
) {

    private var currentRALocation = RALocation()
    private var currentSession = Session()

    fun startLocationComputation(): Observable<RALocation> = Observable.defer {
        var lastLocation: Location? = null
        currentSession.startDate = System.currentTimeMillis()
        return@defer locationManager.requestLocationUpdates().timeInterval()
            .map {
                val location = it.value()
                val distance = if (lastLocation == null) 0f else location.distanceTo(lastLocation)
                val fullDistance =
                    if (lastLocation == null) 0f else currentRALocation.fullDistance + distance
                val currentSpeed =
                    if (lastLocation == null) 0f else distance / it.time(TimeUnit.SECONDS)
                val currentKmTime =
                    if (lastLocation == null || distance == 0f) Int.MAX_VALUE else (it.time(TimeUnit.SECONDS) * M_TO_KM / distance).toInt()
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
                currentRALocation
            }.filter {
                it.currentSpeed > LOW_SPEED_THRESHOLD
            }.doOnNext {
                currentSession.locations.add(it)
            }
    }.doOnError {
        Timber.e(it)
    }

    fun stopLocationComputation() = Completable.defer {
        currentSession.endDate = System.currentTimeMillis()
        currentSession = Session()
        currentRALocation = RALocation()
        locationManager.stopLocationUpdates()
        Completable.complete()
    }

    fun saveCurrentSession(): Completable = Completable.defer {
        storageManager.saveSession(sessionDBEntityDataMapper.transform(currentSession))
        stopLocationComputation()
    }

    fun getAllSessions(): Single<List<Session>> = Single.defer {
        Single.just(sessionDBEntityDataMapper.transformEntity(storageManager.getAllSessions()).filter { it.locations.isNotEmpty() })

    }

}