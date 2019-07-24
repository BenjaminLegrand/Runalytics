package fr.legrand.runalytics.data.repository

import android.location.Location
import fr.legrand.runalytics.data.location.LocationManager
import io.reactivex.Observable
import timber.log.Timber

class LocationRepository(
        private val locationManager: LocationManager
) {

    fun startLocationComputation(): Observable<Float> = Observable.defer {
        var lastLocation: Location? = null
        return@defer locationManager.requestLocationUpdates()
                .map {
                    if(lastLocation == null) return@map 0f
                    val distance = it.distanceTo(lastLocation)
                    lastLocation = it
                    distance
                }
    }.doOnError {
        Timber.e(it)
    }

}