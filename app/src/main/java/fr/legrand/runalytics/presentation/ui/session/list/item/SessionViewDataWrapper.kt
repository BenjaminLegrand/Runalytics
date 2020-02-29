package fr.legrand.runalytics.presentation.ui.session.list.item

import android.annotation.SuppressLint
import android.content.Context
import fr.legrand.runalytics.R
import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.presentation.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd MMMM yyyy HH:mm:ss"
private const val MS_TO_S = 1000

class SessionViewDataWrapper(
    private val session: Session
) {
    fun getDurationText(context: Context): String =
        session.locations.lastOrNull()?.timestamp?.div(MS_TO_S)?.let {
            val time = TimeUtils.extractTime(it)
            return@let if (time.first == 0L && time.second == 0L) {
                context.getString(
                    R.string.view_session_duration_s_format,
                    time.third
                )
            } else if (time.first == 0L) {
                context.getString(
                    R.string.view_session_duration_ms_format,
                    time.second, time.third
                )
            } else {
                context.getString(
                    R.string.view_session_duration_hms_format,
                    time.first, time.second, time.third
                )
            }
        } ?: context.getString(R.string.unknown_duration)

    @SuppressLint("SimpleDateFormat")
    fun getStartDateText(): String = SimpleDateFormat(DATE_FORMAT).format(Date(session.startDate))

}