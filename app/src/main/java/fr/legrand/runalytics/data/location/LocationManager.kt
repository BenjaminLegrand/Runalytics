package fr.legrand.runalytics.data.location

import android.location.Location
import io.reactivex.Observable

interface LocationManager {
    fun requestLocationUpdates(): Observable<Location>
}