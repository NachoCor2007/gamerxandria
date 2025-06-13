package com.austral.gamerxandria

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.austral.gamerxandria.nav.BottomBar
import com.austral.gamerxandria.nav.NavHostComposable
import com.austral.gamerxandria.notification.notificationChannelID
import com.austral.gamerxandria.tab.PermissionsWrapper
import com.austral.gamerxandria.ui.theme.GamerxandriaTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        createNotificationChannel()

        enableEdgeToEdge()
        setContent {
            GamerxandriaTheme {
                PermissionsWrapper {
                    val navController = rememberNavController()

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            BottomBar(navController::navigate)
                        }
                    ) { innerPadding ->
                        NavHostComposable(innerPadding, navController)
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            notificationChannelID,
            "Gamerxandria Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)
    }
}

