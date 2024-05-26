package com.example.mazdacompanionapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isNotificationServiceEnabled()) {
            requestNotificationPermission()
        }

        setContent {
            MazdaCompanionAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NotificationList()
                }
            }
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(this)
        return packageNames.contains(packageName)
    }

    private fun requestNotificationPermission() {
        Toast.makeText(this, "Please grant notification access", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }
}

@Composable
fun NotificationList() {
    val notifications = NotificationListener.notificationsLiveData.observeAsState(emptyList())
    LazyColumn {
        items(notifications.value) { notification ->
            NotificationItem(notification)
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationData) {
    Column(modifier = Modifier.padding(16.dp)) {
        notification.title?.let { Text(text = "Title: $it", style = MaterialTheme.typography.h6) }
        notification.text?.let { Text(text = "Text: $it", style = MaterialTheme.typography.body1) }
        Text(text = "Time: ${formatTimestamp(notification.timestamp)}", style = MaterialTheme.typography.caption)
        Text(text = "Package: ${notification.packageName}", style = MaterialTheme.typography.body2)
        Text(text = "Notification ID: ${notification.notificationId}", style = MaterialTheme.typography.body2)
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MazdaCompanionAppTheme {
        NotificationList()
    }
}
