package com.example.mazdacompanionapp
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import com.example.mazdacompanionapp.ui.theme.MazdaCompanionAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isNotificationServiceEnabled()) {
            requestNotificationPermission()
        }

        setContent {
            MazdaCompanionAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NotificationCount()
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
fun NotificationCount() {
    val notificationCount = NotificationListener.notificationsLiveData.observeAsState(0)
    Text(
        text = "Počet notifikácií: ${notificationCount.value}",
        style = MaterialTheme.typography.h4
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MazdaCompanionAppTheme {
        NotificationCount()
    }
}
