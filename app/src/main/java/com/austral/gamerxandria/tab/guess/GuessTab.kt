package com.austral.gamerxandria.tab.guess

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.austral.gamerxandria.R

@Composable
fun GuessTab() {
    Column {
        Box(modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
            Image(
                painterResource(R.drawable.stand_by_image),
                contentDescription = "Stand ready for my arrival, worm",
            )
        }
        TextField(
            value = "",
            placeholder = { Text("Guess the game") },
            onValueChange = {  },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .fillMaxWidth()
                .padding(0.dp, 16.dp)
                .background(Color(0xFFEC7C7C))
                .padding(8.dp, 16.dp)

        ) {
            Text(
                text = "Bad guess",
            )
        }

        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .fillMaxWidth()
                .padding(0.dp, 16.dp)
                .background(Color(0xFFECD07C))
                .padding(8.dp, 16.dp)

        ) {
            Text(
                text = "Near guess",
            )
        }

        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .fillMaxWidth()
                .padding(0.dp, 16.dp)
                .background(Color(0xFF8DEC7C))
                .padding(8.dp, 16.dp)

        ) {
            Text(
                text = "Guessed!",
            )
        }
    }
}
