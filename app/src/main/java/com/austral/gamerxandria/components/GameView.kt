package com.austral.gamerxandria.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.austral.gamerxandria.R
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
            Image(
                painterResource(R.drawable.stand_by_image),
                contentDescription = "Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .height(256.dp)
            )

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
