package com.austral.gamerxandria.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NotFound(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFF37272)).padding(16.dp)
    ) {
        Text(
            text = message,
            color = Color(0xFFF84040),
        )
    }
}
