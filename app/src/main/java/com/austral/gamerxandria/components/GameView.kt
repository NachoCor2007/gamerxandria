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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.tab.NotFound
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.CardBackground
import com.austral.gamerxandria.ui.theme.GameViewTitle

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
        VideoGameTitle(videoGame)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState(), enabled = true)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                )
            ) {
                AsyncImage(
                    model = videoGame.cover.url,
                    contentDescription = "VideoGame cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = "Game status",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = videoGame.first_release_date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = "${videoGame.aggregated_rating}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = videoGame.platforms.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = videoGame.genres.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = videoGame.involved_companies.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = "Summary",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = videoGame.summary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
        }
    }
}

@Composable
private fun VideoGameTitle(videoGame: VideoGame) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, AppSize.spacingSmall),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = videoGame.name,
            style = GameViewTitle,
            textAlign = TextAlign.Center
        )
    }
}
