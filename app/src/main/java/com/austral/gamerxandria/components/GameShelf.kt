package com.austral.gamerxandria.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.austral.gamerxandria.model.Shelf
import com.austral.gamerxandria.model.VideoGame

@Composable
fun GameShelf(navigateToGameView: (Int) -> Unit, shelf: Shelf) {
    val viewModel = hiltViewModel<GameShelfViewModel>()
    val videoGames = viewModel.retrieveVideoGamesByIds(shelf.games)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)
    ) {
        Text(
            text = shelf.name,
            fontSize = 32.sp
        )
        LazyRow {
            item {
                videoGames.forEach{ videoGame ->
                    GameCard(
                        navigateToGameView = navigateToGameView,
                        videoGame = videoGame
                    )
                }
            }
        }
    }
}

@Composable
fun GameCard(navigateToGameView: (Int) -> Unit, videoGame: VideoGame) {
    Card(
        onClick = { navigateToGameView(videoGame.id) },
        modifier = Modifier
            .padding(4.dp)
            .size(192.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image filling the entire card
            AsyncImage(
                model = videoGame.cover.url,
                contentDescription = "VideoGame cover",
                contentScale = ContentScale.Crop,  // This ensures the image covers the whole area
                modifier = Modifier.fillMaxSize()
            )

            // Semi-transparent overlay to make text more readable
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,  // Top of the box
                            endY = Float.POSITIVE_INFINITY  // Bottom of the box
                        )
                    )
            )

            // Text at the bottom
            Text(
                text = videoGame.name,
                fontSize = 24.sp,
                color = Color.White,  // Light text color for better contrast
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}
