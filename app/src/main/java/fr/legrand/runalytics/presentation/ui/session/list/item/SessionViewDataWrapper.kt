package fr.legrand.runalytics.presentation.ui.session.list.item

import android.annotation.SuppressLint
import android.content.Context
import fr.legrand.runalytics.R
import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.presentation.ui.session.running.item.RALocationViewDataWrapper
import fr.legrand.runalytics.presentation.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.Date

private const val DATE_FORMAT = "dd MMMM yyyy HH:mm:ss"
private const val MS_TO_S = 1000

class SessionViewDataWrapper(
    private val session: Session
) {

    private val locations = session.locations.map { RALocationViewDataWrapper(it) }

    fun getDurationText(context: Context): String {
        if(session.startDate <= 0L || session.endDate <= 0L){
            return context.getString(R.string.view_session_list_item_duration_unknown)
        }
        val time = TimeUtils.extractTimeText(context, (session.endDate - session.startDate) / MS_TO_S)
        return context.getString(R.string.view_session_list_item_duration_format, time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getStartDateText(): String = SimpleDateFormat(DATE_FORMAT).format(Date(session.startDate))

    fun getLastLocation(): RALocationViewDataWrapper? = locations.lastOrNull()
    fun getFullDistanceKm(): Float = session.traveledDistance
    fun getAltitudeDiff(): Float = session.altitudeDiff

}