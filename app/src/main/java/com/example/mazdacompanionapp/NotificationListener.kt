package com.example.mazdacompanionapp

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val notificationText = notification.extras.getCharSequence("android.text")?.toString()
        if (notificationText != null) {
            notifications.add(notificationText)
            notificationsLiveData.postValue(notifications.size)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val notificationText = notification.extras.getCharSequence("android.text")?.toString()
        if (notificationText != null) {
            notifications.remove(notificationText)
            notificationsLiveData.postValue(notifications.size)
        }
    }

    companion object {
        val notifications: MutableList<String> = mutableListOf()
        val notificationsLiveData: MutableLiveData<Int> = MutableLiveData(0)
    }
}
