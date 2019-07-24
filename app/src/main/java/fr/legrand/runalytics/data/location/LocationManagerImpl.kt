package fr.legrand.runalytics.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import fr.legrand.runalytics.data.values.LocationValues
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LocationManagerImpl(context: Context) : LocationManager {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locCallback: LocationCallback

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(): Observable<Location> =
        Observable.create<Location> { emitter ->
            val locRequest =
                LocationRequest().setFastestInterval(LocationValues.LOCATION_REQUEST_UPDATE_TIME)
                    .setInterval(LocationValues.LOCATION_REQUEST_UPDATE_TIME)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            locCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    emitter.onNext(locationResult.lastLocation)
                }
            }
            Looper.prepare()
            locationClient.requestLocationUpdates(locRequest, locCallback, null)
        }.observeOn(Schedulers.io()).doOnDispose {
            locationClient.removeLocationUpdates(locCallback)
        }
}