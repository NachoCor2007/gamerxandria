package com.austral.gamerxandria.tab

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsWrapper(content: @Composable () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val postNotificationPermission = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )

        LaunchedEffect(key1 = true) {
            if (!postNotificationPermission.status.isGranted) {
                postNotificationPermission.launchPermissionRequest()
            }
        }
    }

    content()
}