package fr.legrand.runalytics.data.repository

import android.location.Location
import fr.legrand.runalytics.data.component.LogComponent
import fr.legrand.runalytics.data.exception.EmptySessionException
import fr.legrand.runalytics.data.manager.location.LocationManager
import fr.legrand.runalytics.data.manager.storage.StorageManager
import fr.legrand.runalytics.data.mapper.SessionDBEntityDataMapper
import fr.legrand.runalytics.data.model.RALocation
import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.data.model.SessionState
import fr.legrand.runalytics.data.utils.LocationUtils
import fr.legrand.runalytics.data.values.LocationValues
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

private const val M_TO_KM = 1000
private const val LOW_SPEED_THRESHOLD = 2
private const val SESSION_TIMER_STEP = 1L //in sec

class LocationRepository(
    private val locationManager: LocationManager,
    private val storageManager: StorageManager,
    private val logComponent: LogComponent,
    private val sessionDBEntityDataMapper: SessionDBEntityDataMapper
) {

    private var currentSession = Session()

    fun startLocationComputation(): Observable<Pair<SessionState, Session>> = Observable.defer {
        currentSession = Session()
        currentSession.id = System.currentTimeMillis()
        currentSession.startDate = System.currentTimeMillis()
        logComponent.startSession(currentSession.id)
        var lastLocation: Location? = null
        logComponent.i("New session : $currentSession")
        val sessionObservable = locationManager.requestLocationUpdates()
            .timeInterval().map {
                val distance = if (lastLocation == null) LocationValues.NO_DISTANCE else it.value().distanceTo(lastLocation)
                val speed = if (lastLocation == null) LocationValues.NO_SPEED else distance / it.time(TimeUnit.SECONDS)
                lastLocation = it.value()
                Triple(it.value(), speed, distance)
            }.map {
                logComponent.i("Location data : $it")
                if (it.third == LocationValues.NO_DISTANCE || it.second == LocationValues.NO_SPEED) {
                    return@map Pair(SessionState.WAITING_FOR_LOCATION, currentSession)
                } else if (it.first.accuracy > LocationValues.MIN_LOCATION_ACCURACY) {
                    return@map Pair(SessionState.LOW_ACCURACY, currentSession)
                } else if (it.second < LOW_SPEED_THRESHOLD) {
                    return@map Pair(SessionState.LOW_SPEED, currentSession)
                }
                val location = it.first
                val speed = it.second
                val distance = it.third

                currentSession.traveledDistance = currentSession.traveledDistance + distance
                logComponent.i("Traveled distance : ${currentSession.traveledDistance}")

//                lastLocation?.let {
//                    currentSession.altitudeDiff =
//                        (currentSession.altitudeDiff + (it.altitude - location.altitude)).toFloat()
//                }

                val currentLocation = RALocation(
                    speed,
                    distance,
                    location.altitude.toFloat(),
                    System.currentTimeMillis()
                )
                currentSession.locations.add(currentLocation)
                logComponent.i("Current location : $currentLocation")

                val kmSplitLocations = LocationUtils.splitByKilometer(currentSession.locations)
                val kmSplitTimes = mutableListOf<Long>()
                kmSplitLocations.take(kotlin.math.max(kmSplitLocations.size - 1, 0)).forEach {
                    kmSplitTimes.add((it.last().timestamp - it.first().timestamp) / LocationValues.MS_TO_S)
                }
                kmSplitLocations.lastOrNull()?.let { lastKmSplit ->
                    val remainingDistance =
                        LocationValues.KILOMETER_VALUE - lastKmSplit.sumByDouble { it.distance.toDouble() }
                    val remainingTime = remainingDistance / lastKmSplit.last().speed
                    val elapsedTime =
                        (lastKmSplit.last().timestamp - lastKmSplit.first().timestamp) / LocationValues.MS_TO_S
                    kmSplitTimes.add((elapsedTime + remainingTime).toLong())
                }
                currentSession.kmTimeList = kmSplitTimes

                logComponent.i("Km split times : $kmSplitTimes")

                Pair(SessionState.VALID, currentSession)
            }

        return@defer Observable.concat(
            Observable.just(
                Pair(
                    SessionState.WAITING_FOR_LOCATION,
                    currentSession
                )
            ), sessionObservable
        )
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
        if (currentSession.locations.isEmpty()) {
            throw EmptySessionException()
        }
        logComponent.i("Saving session : $currentSession")
        storageManager.saveSession(sessionDBEntityDataMapper.transform(currentSession))
        currentSession = Session()
        Completable.complete()
    }

    fun getAllSessions(): Single<List<Session>> = Single.defer {
        Single.just(sessionDBEntityDataMapper.transformEntity(storageManager.getAllSessions()).filter { it.locations.isNotEmpty() })
    }

    fun startSessionTimer(): Observable<Long> =
        Observable.interval(SESSION_TIMER_STEP, TimeUnit.SECONDS)

}