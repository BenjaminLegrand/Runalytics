package fr.legrand.runalytics.presentation.utils

import java.util.concurrent.TimeUnit


private const val H_TO_M = 60
private const val H_TO_S = 3600
private const val S_TO_M = 60

object TimeUtils {

    fun extractTime(duration: Long): Triple<Long, Long, Long> {
        val hours = TimeUnit.SECONDS.toHours(duration)
        val minutes = TimeUnit.SECONDS.toMinutes(duration) - hours * H_TO_M
        val seconds = duration - hours * H_TO_S - minutes * S_TO_M
        return Triple(hours, minutes, seconds)
    }
}