package com.austral.gamerxandria.tab.library

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.austral.gamerxandria.components.Shelf

@Composable
fun LibraryTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            repeat(10) {
                Shelf()
            }
        }
    }
}
