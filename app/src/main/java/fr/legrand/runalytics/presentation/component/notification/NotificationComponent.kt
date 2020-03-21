package fr.legrand.runalytics.presentation.component.notification

import android.app.Notification

interface NotificationComponent {
    fun updateSessionNotification(notificationId: Int, timer: Long)
    fun getNewSessionNotification(): Notification
}