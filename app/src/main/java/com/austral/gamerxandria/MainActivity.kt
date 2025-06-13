package com.austral.gamerxandria

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.austral.gamerxandria.nav.BottomBar
import com.austral.gamerxandria.nav.NavHostComposable
import com.austral.gamerxandria.ui.theme.GamerxandriaTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

            LaunchedEffect(key1 = true) {
                if (!postNotificationPermission.status.isGranted) {
                    postNotificationPermission.launchPermissionRequest()
                }
            }

            GamerxandriaTheme {
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
