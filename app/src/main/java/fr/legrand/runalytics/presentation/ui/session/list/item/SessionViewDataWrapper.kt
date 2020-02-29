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
            val time = TimeUtils.extractTimeText(context, it)
            return@let context.getString(R.string.view_session_list_item_duration_format, time)
        } ?: context.getString(R.string.unknown_duration)

    @SuppressLint("SimpleDateFormat")
    fun getStartDateText(): String = SimpleDateFormat(DATE_FORMAT).format(Date(session.startDate))

}