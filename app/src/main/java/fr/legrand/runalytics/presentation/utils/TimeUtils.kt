package fr.legrand.runalytics.presentation.utils

import android.content.Context
import fr.legrand.runalytics.R
import java.util.concurrent.TimeUnit


private const val H_TO_M = 60
private const val H_TO_S = 3600
private const val S_TO_M = 60

object TimeUtils {

    fun extractTimeText(context: Context, duration: Long): String {
        val hours = TimeUnit.SECONDS.toHours(duration)
        val minutes = TimeUnit.SECONDS.toMinutes(duration) - hours * H_TO_M
        val seconds = duration - hours * H_TO_S - minutes * S_TO_M
        return if (hours == 0L && minutes == 0L) {
            context.getString(
                R.string.duration_s_format,
                seconds
            )
        } else if (hours == 0L) {
            context.getString(
                R.string.duration_ms_format,
                minutes, seconds
            )
        } else {
            context.getString(
                R.string.duration_hms_format,
                hours, minutes, seconds
            )
        }
    }

    fun extractTime(duration: Long): Triple<Long, Long, Long> {
        val hours = TimeUnit.SECONDS.toHours(duration)
        val minutes = TimeUnit.SECONDS.toMinutes(duration) - hours * H_TO_M
        val seconds = duration - hours * H_TO_S - minutes * S_TO_M
        return Triple(hours, minutes, seconds)
    }
}