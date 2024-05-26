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
        fetchExistingNotifications()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notificationData = extractNotificationData(sbn)
        notifications.add(notificationData)
        notificationsLiveData.postValue(notifications.toList())
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val notificationData = extractNotificationData(sbn)
        notifications.remove(notificationData)
        notificationsLiveData.postValue(notifications.toList())
    }
    private fun fetchExistingNotifications() {
        val activeNotifications = activeNotifications
        val existingNotifications = mutableListOf<NotificationData>()

        for (notification in activeNotifications) {
            val notificationData = extractNotificationData(notification)
            existingNotifications.add(notificationData)
        }

        notifications.addAll(existingNotifications)
        notificationsLiveData.postValue(notifications.toList())
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
        val notifications: MutableList<NotificationData> = mutableListOf()
        val notificationsLiveData: MutableLiveData<List<NotificationData>> = MutableLiveData()
    }
}

