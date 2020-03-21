package fr.legrand.runalytics.presentation.ui.session.running.item

import android.content.Context
import fr.legrand.runalytics.R
import fr.legrand.runalytics.data.model.RALocation

private const val MS_TO_S = 1000
private const val KMH_TO_MS = 3.6

data class RALocationViewDataWrapper(private val location: RALocation) {
    fun getSpeedText(context: Context): String =
        context.getString(
            R.string.running_session_location_speed_format,
            location.speed * KMH_TO_MS
        )

    fun getSpeed(): Float = location.speed
    fun getFloatTimestamp(): Float = location.timestamp.toFloat()
}