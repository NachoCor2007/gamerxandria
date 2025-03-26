package com.austral.gamerxandria.tab.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.austral.gamerxandria.components.GameCollection

@Composable
fun SearchTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Search bar stand by"
            )
            GameCollection("Based on your library")
            GameCollection("Trending")
            GameCollection("This week")
            GameCollection("Another topic")
        }
    }
}
