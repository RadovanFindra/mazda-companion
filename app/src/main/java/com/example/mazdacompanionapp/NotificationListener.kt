package com.example.mazdacompanionapp

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Pridať novú notifikáciu do zoznamu notifikácií
        val notificationText = sbn.notification.extras.getCharSequence("android.text")?.toString()
        notificationText?.let {
            notifications.add(it)
            // Aktualizovať UI
            updateUI()
        }
    }
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Odstrániť notifikáciu zo zoznamu notifikácií
        val notificationText = sbn.notification.extras.getCharSequence("android.text")?.toString()
        notificationText?.let {
            notifications.remove(it)
            // Aktualizovať UI
            updateUI()
        }
    }

    companion object {
        val notifications: MutableList<String> = mutableListOf()
        val notificationsLiveData: MutableLiveData<List<String>> = MutableLiveData()

        private fun updateUI() {
            // Aktualizovať UI na zobrazenie nových notifikácií
            notificationsLiveData.postValue(notifications.toList())
        }
    }
}
