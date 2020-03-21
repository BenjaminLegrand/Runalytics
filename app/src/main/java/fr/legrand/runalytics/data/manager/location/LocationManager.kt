package fr.legrand.runalytics.data.manager.location

import android.location.Location
import io.reactivex.Observable

interface LocationManager {
    fun requestLocationUpdates(): Observable<Location>
    fun stopLocationUpdates()
}