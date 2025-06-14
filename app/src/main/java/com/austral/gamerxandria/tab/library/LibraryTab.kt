package com.austral.gamerxandria.tab.library

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.austral.gamerxandria.components.GameShelf
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.gamerxandria.R
import com.austral.gamerxandria.components.ShelfCreatorPopUp
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.AppSize.textMedium
import com.austral.gamerxandria.ui.theme.ButtonRed

@Composable
fun LibraryTab(navigateToGameView: (Int) -> Unit) {
    val context = LocalContext.current

    val viewModel = hiltViewModel<LibraryViewModel>()
    val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.authenticate(context)
    }

    val biometricManager = remember { BiometricManager.from(context) }

    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }

    when (isBiometricAvailable) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            if (isAuthenticated) {
                LibraryBody(viewModel, navigateToGameView)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.authentication_required)
                    )
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.biometric_not_available))
            }
        }
    }
}

@Composable
private fun LibraryBody(
    viewModel: LibraryViewModel,
    navigateToGameView: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val shelves by viewModel.shelves.collectAsState(listOf())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (shelves.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.no_shelves_message),
                        fontSize = textMedium
                    )
                }
            } else { shelves.forEach { shelf -> GameShelf(navigateToGameView, shelf) } }
        }

        var showDialog by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(AppSize.noSpace, AppSize.noSpace, AppSize.noSpace, AppSize.contentPadding),
            containerColor = ButtonRed,
            elevation = FloatingActionButtonDefaults.elevation()
        ) {
            Icon(
                Icons.Default.Add,
                stringResource(R.string.add_collection_fab_description)
            )
        }

        // Text input dialog
        if (showDialog) {
            ShelfCreatorPopUp(
                onDismiss = { showDialog = false },
                onConfirm = { newShelfName ->
                    if (newShelfName.isNotBlank()) {
                        viewModel.addShelf(newShelfName)
                    }

                    showDialog = false
                }
            )
        }
    }
}
