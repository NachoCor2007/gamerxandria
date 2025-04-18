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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.austral.gamerxandria.components.GameCollection
import androidx.compose.runtime.*
import com.austral.gamerxandria.components.ShelfCreatorPopUp

@Composable
fun LibraryTab(navigateToGameView: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            repeat(4) {
                GameCollection(navigateToGameView)
            }
        }

        var showDialog by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 0.dp, 16.dp) // Add padding to avoid touching screen edges
        ) {
            Icon(
                Icons.Default.Add,
                "Add collection"
            )
        }

        // Text input dialog
        if (showDialog) {
            ShelfCreatorPopUp(
                onDismiss = { showDialog = false },
                onConfirm = { text ->
//                    if (text.isNotBlank()) {
//                        items = items + MyItem(nextId, text)
//                        nextId++
//                    }

                    println("Creating shelf with name: $text")
                    showDialog = false
                }
            )
        }
    }
}
