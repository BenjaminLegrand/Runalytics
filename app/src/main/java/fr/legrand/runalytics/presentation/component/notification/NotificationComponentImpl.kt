package fr.legrand.runalytics.presentation.component.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.utils.TimeUtils

private const val CHANNEL_ID = "RUNALYTICS_CHANNEL_ID"
private const val HIGH_CHANNEL_ID = "HIGH_CHANNEL_ID"
private const val CHANNEL_NAME = "RUNALYTICS_CHANNEL"
private const val HIGH_CHANNEL_NAME = "HIGH_CHANNEL"

class NotificationComponentImpl(
    private val context: Context,
    private val notificationManager: NotificationManager
) : NotificationComponent {

    override fun getNewSessionNotification(): Notification =
        getBaseHighPriorityNotificationBuilder()
            .setContentText(context.getString(R.string.notification_new_session))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.notification_new_session))
            )
            .build()

    override fun updateSessionNotification(notificationId: Int, timer: Long) {
        val timerText = TimeUtils.extractTimeText(context, timer)
        notificationManager.notify(
            notificationId,
            getBaseDefaultPriorityNotificationBuilder()
                .setContentText(context.getString(R.string.notification_session_running, timerText))
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            context.getString(
                                R.string.notification_session_running,
                                timerText
                            )
                        )
                )
                .build()
        )
    }

    private fun getBaseHighPriorityNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, getHighChannelId())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    private fun getBaseDefaultPriorityNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, getDefaultChannelId())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun getDefaultChannelId(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationManager.createNotificationChannel(channel)
        }
        return CHANNEL_ID
    }

    private fun getHighChannelId(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    HIGH_CHANNEL_ID,
                    HIGH_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(channel)
        }
        return HIGH_CHANNEL_ID
    }


}