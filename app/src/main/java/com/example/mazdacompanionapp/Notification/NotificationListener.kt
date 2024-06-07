package com.example.mazdacompanionapp.Notification

import android.app.Notification
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import org.json.JSONObject

data class NotificationData(
    val appName: String?,
    val title: String?,
    val text: String?,
    val timestamp: Long
)

class NotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
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
        val packageName = sbn.packageName
        val appName = getAppName(packageName)
        val title = notification.extras.getString(Notification.EXTRA_TITLE)
        val text = notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        val timestamp = sbn.postTime

        return NotificationData(
            appName = appName,
            title = title,
            text = text,
            timestamp = timestamp
        )
    }

    private fun getAppName(packageName: String): String? {
        val packageManager = applicationContext.packageManager
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    companion object {

        val notificationsLiveData: MutableLiveData<List<NotificationData>> = MutableLiveData()

        fun notificationsToJson(): JSONObject {
            val notificationsList = notificationsLiveData.value ?: emptyList()

            val jsonArray = JSONArray()
            for (notification in notificationsList) {
                val jsonObject = JSONObject().apply {
                    put("appName", notification.appName)
                    put("title", notification.title)
                    put("text", notification.text)
                }
                jsonArray.put(jsonObject)
            }
            return JSONObject().apply { put("notifications", jsonArray) }
        }
    }
}