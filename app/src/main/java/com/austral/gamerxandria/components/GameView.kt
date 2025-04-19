package com.austral.gamerxandria.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.tab.NotFound

@Composable
fun GameView(videoGameId: Int) {
    val viewModel = hiltViewModel<GameViewModel>()
    val videoGame = viewModel.retrieveVideoGameById(videoGameId)

    when (videoGame) {
        null -> NotFound("VideoGame not found. id: $videoGameId")
        else -> VideoGameInformation(videoGame)
    }
}

@Composable
private fun VideoGameInformation(videoGame: VideoGame) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = videoGame.name,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState(), enabled = true)
        ) {
            Card {
                AsyncImage(
                    model = videoGame.cover.url,
                    contentDescription = "VideoGame cover",
                    contentScale = ContentScale.Crop,  // This ensures the image covers the whole area
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = "Game status",
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = videoGame.first_release_date,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "${videoGame.aggregated_rating}",
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = videoGame.platforms.toString(),
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = videoGame.genres.toString(),
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = videoGame.involved_companies.toString(),
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Summary",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = videoGame.summary,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
