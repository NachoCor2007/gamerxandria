package com.austral.gamerxandria.tab.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.austral.gamerxandria.components.GameCollection

@Composable
fun SearchTab() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
            Text(
                text = "Search bar stand by"
            )
            GameCollection("Based on your library")
            GameCollection("Trending")
            GameCollection("This week")
            GameCollection("Another topic")
        }
}
