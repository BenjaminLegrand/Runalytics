package fr.legrand.runalytics.data.repository

import android.location.Location
import fr.legrand.runalytics.data.location.LocationManager
import fr.legrand.runalytics.data.model.RALocation
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LocationRepository(
    private val locationManager: LocationManager
) {

    private var currentRALocation = RALocation()

    fun startLocationComputation(): Observable<RALocation> = Observable.defer {
        var lastLocation: Location? = null
        return@defer locationManager.requestLocationUpdates().timeInterval()
            .map {
                val location = it.value()
                val distance = if (lastLocation == null) 0f else location.distanceTo(lastLocation)
                val fullDistance =
                    if (lastLocation == null) 0f else currentRALocation.fullDistance + distance
                val currentDistance =
                    if (lastLocation == null) 0f else distance / it.time(TimeUnit.SECONDS)
                val timestamp =
                    if (lastLocation == null) 0L else currentRALocation.timestamp + it.time()
                val fullAltitudeDiff = if (lastLocation == null) 0f else
                    currentRALocation.fullAltitudeDiff + location.altitude.toFloat() - currentRALocation.altitude
                lastLocation = location
                currentRALocation = currentRALocation.copy(
                    currentSpeed = currentDistance,
                    lastDistance = distance,
                    fullDistance = fullDistance,
                    altitude = location.altitude.toFloat(),
                    fullAltitudeDiff = fullAltitudeDiff,
                    timestamp = timestamp
                )
                currentRALocation
            }
    }.doOnError {
        Timber.e(it)
    }

}