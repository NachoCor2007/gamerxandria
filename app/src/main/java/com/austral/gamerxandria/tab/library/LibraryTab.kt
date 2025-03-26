package com.austral.gamerxandria.tab.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LibraryTab() {
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.Red)
    ) {
        Text(
            text = "My library",
            fontSize = 32.sp
        )
        Shelf()
    }
}

@Composable
fun Shelf() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp).background(Color.Green)
    ) {
        Text(
            text = "Colection A"
        )
        Row {
            for (i in 1..10) {
                GameCard()
            }
        }
    }
}

@Composable
fun GameCard() {
    Card(
        modifier = Modifier.padding(4.dp).size(192.dp)
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Game",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
