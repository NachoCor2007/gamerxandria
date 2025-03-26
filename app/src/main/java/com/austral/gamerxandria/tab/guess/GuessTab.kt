package com.austral.gamerxandria.tab.guess

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
    }
}
