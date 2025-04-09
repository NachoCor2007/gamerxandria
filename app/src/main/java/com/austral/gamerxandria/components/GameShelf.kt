package com.austral.gamerxandria.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameCollection(navigateToGameView: () -> Unit, shelfTitle: String = "A collection") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)
    ) {
        Text(
            text = shelfTitle,
            fontSize = 32.sp
        )
        LazyRow {
            item {
                repeat(10) {
                    GameCard(navigateToGameView)
                }
            }
        }
    }
}

@Composable
fun GameCard(navigateToGameView: () -> Unit) {
    Card(
        onClick = navigateToGameView,
        modifier = Modifier
            .padding(4.dp)
            .size(192.dp)
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Game",
                fontSize = 24.sp,
            )
        }
    }
}
