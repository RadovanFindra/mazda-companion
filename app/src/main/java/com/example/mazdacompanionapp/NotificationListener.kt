package com.example.mazdacompanionapp

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData

data class NotificationData(
    val title: String?,
    val text: String?,
    val timestamp: Long,
    val packageName: String,
    val icon: Int,
    val notificationId: Int
)

class NotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        // Získanie existujúcich notifikácií pri pripojení služby
        fetchExistingNotifications()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        refreshNotificationList()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        refreshNotificationList()
    }

    private fun fetchExistingNotifications() {
        val activeNotifications = activeNotifications
        val newNotifications = mutableListOf<NotificationData>()

        for (notification in activeNotifications) {
            val notificationData = extractNotificationData(notification)
            newNotifications.add(notificationData)
        }

        notificationsLiveData.postValue(newNotifications.toList())
    }

    private fun refreshNotificationList() {
        fetchExistingNotifications()
    }

    private fun extractNotificationData(sbn: StatusBarNotification): NotificationData {
        val notification = sbn.notification
        val title = notification.extras.getString("android.title")
        val text = notification.extras.getCharSequence("android.text")?.toString()
        val timestamp = sbn.postTime
        val packageName = sbn.packageName
        val icon = notification.icon
        val notificationId = sbn.id

        return NotificationData(
            title = title,
            text = text,
            timestamp = timestamp,
            packageName = packageName,
            icon = icon,
            notificationId = notificationId
        )
    }

    companion object {
        val notificationsLiveData: MutableLiveData<List<NotificationData>> = MutableLiveData()
    }
}


