package com.austral.gamerxandria.tab.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.austral.gamerxandria.R
import com.austral.gamerxandria.ui.theme.AppSize
import com.google.firebase.auth.FirebaseUser

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ProfileTab() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val viewModel = hiltViewModel<ProfileViewModel>()
        val userData = viewModel.userData.collectAsStateWithLifecycle()

        if (userData.value == null) {
            LoginWithGoogleButton(
                onClick = viewModel::launchCredentialManager
            )
        } else {
            // Display user data or profile information
            ProfileContent(userData.value!!)
            Spacer(modifier = Modifier.padding(AppSize.spacingLarge))
            Button(onClick = { viewModel.signOut() }) {
                Text(stringResource(R.string.sign_out_message))
            }
        }
    }
}

@Composable
fun LoginWithGoogleButton(onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        border = BorderStroke(AppSize.borderWidth, MaterialTheme.colorScheme.primary),
        onClick = onClick
    ) {
        Image(
            modifier = Modifier.size(AppSize.spacingLarge),
            painter = painterResource(R.drawable.google_logo__streamline_ultimate),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(AppSize.spacingSmall))
        Text(stringResource(R.string.login_with_google))
    }
}

@Composable
fun ProfileContent(userData: FirebaseUser) {
    Row {
        AsyncImage(
            modifier = Modifier
                .size(AppSize.spacingXXXLarge)
                .clip(CircleShape),
            model = userData.photoUrl,
            contentDescription = stringResource(R.string.user_profile_picture)
        )
        Spacer(modifier = Modifier.width(AppSize.spacingXLarge))
        Column {
            Text(
                text = userData.displayName.toString(),
                fontSize = AppSize.textLarge
            )
            Text(
                text = userData.email.toString(),
            )
        }
    }
}
