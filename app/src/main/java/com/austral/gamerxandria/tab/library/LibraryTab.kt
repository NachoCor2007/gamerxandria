package com.austral.gamerxandria.tab.library

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.austral.gamerxandria.components.GameShelf
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.austral.gamerxandria.R
import com.austral.gamerxandria.components.ShelfCreatorPopUp
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.ButtonRed

@Composable
fun LibraryTab(navigateToGameView: (Int) -> Unit) {
    val viewModel = hiltViewModel<LibraryViewModel>()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val shelves = viewModel.retrieveShelves()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            shelves.forEach { shelf -> GameShelf(navigateToGameView, shelf) }
        }

        var showDialog by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 0.dp, AppSize.contentPadding),
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
