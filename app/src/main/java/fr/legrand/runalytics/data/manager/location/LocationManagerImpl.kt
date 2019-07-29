package fr.legrand.runalytics.data.manager.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import fr.legrand.runalytics.data.values.LocationValues
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LocationManagerImpl(context: Context) : LocationManager {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)
    private val settingsClient = LocationServices.getSettingsClient(context)

    private lateinit var locCallback: LocationCallback

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(): Observable<Location> =
        Observable.create<Location> { emitter ->
            val locRequest =
                LocationRequest().setFastestInterval(LocationValues.LOCATION_REQUEST_UPDATE_TIME)
                    .setInterval(LocationValues.LOCATION_REQUEST_UPDATE_TIME)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            val locSettingsBuilder =
                LocationSettingsRequest.Builder().addLocationRequest(locRequest)

            settingsClient.checkLocationSettings(locSettingsBuilder.build()).addOnSuccessListener {
                Timber.i(it.toString())
                locCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        Timber.i(locationResult?.toString())
                        locationResult ?: return
                        emitter.onNext(locationResult.lastLocation)
                    }
                }
                locationClient.requestLocationUpdates(locRequest, locCallback, null)
            }.addOnFailureListener {
                Timber.e(it)
            }


        }.observeOn(Schedulers.io()).doOnDispose {
            locationClient.removeLocationUpdates(locCallback)
        }

    override fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locCallback)
    }
}