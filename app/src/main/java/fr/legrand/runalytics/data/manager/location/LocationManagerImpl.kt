package fr.legrand.runalytics.data.manager.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import fr.legrand.runalytics.data.component.LogComponent
import fr.legrand.runalytics.data.values.LocationValues
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LocationManagerImpl(
    context: Context,
    private val logComponent: LogComponent
) : LocationManager {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)
    private val settingsClient = LocationServices.getSettingsClient(context)

    private var locCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(): Observable<Location> =
        Observable.create<Location> { emitter ->
            logComponent.i("Starting locations")
            val locRequest =
                LocationRequest().setFastestInterval(LocationValues.LOCATION_REQUEST_UPDATE_TIME)
                    .setInterval(LocationValues.LOCATION_REQUEST_UPDATE_TIME)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            val locSettingsBuilder =
                LocationSettingsRequest.Builder().addLocationRequest(locRequest)

            settingsClient.checkLocationSettings(locSettingsBuilder.build()).addOnSuccessListener {
                logComponent.i("Location settings : $it")
                locCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult?.let {
                            logComponent.i(it.lastLocation.toString())
                            emitter.onNext(it.lastLocation)
                        }
                    }
                }
                locationClient.requestLocationUpdates(locRequest, locCallback, null)
            }.addOnFailureListener {
                logComponent.e(it.message)
            }
        }.observeOn(Schedulers.io()).doOnDispose {
            logComponent.i("Disposing locations")
            locCallback?.let {
                locationClient.removeLocationUpdates(it)
            }
        }.unsubscribeOn(Schedulers.io())

    override fun stopLocationUpdates() {
        logComponent.i("Stopping locations")
        locCallback?.let {
            locationClient.removeLocationUpdates(it)
        }
    }
}