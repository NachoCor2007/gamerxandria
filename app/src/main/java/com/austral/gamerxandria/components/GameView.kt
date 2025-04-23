package com.austral.gamerxandria.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.tab.NotFound
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.CardBackground
import com.austral.gamerxandria.ui.theme.GameViewTitle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun GameView(videoGameId: Int) {
    val viewModel = hiltViewModel<GameViewModel>()
    val videoGame = viewModel.videoGame.collectAsStateWithLifecycle().value
    val loading = viewModel.loading.collectAsStateWithLifecycle().value
    val showRetry = viewModel.showRetry.collectAsStateWithLifecycle().value

    if (loading) {
        CircularProgressIndicator(
            color = Color.Gray,
            modifier = Modifier.size(48.dp)
        )
    } else if (showRetry) {
        Text(
            "There was an error"
        )
        Button(
            onClick = { viewModel.retryApiCall() }
        ) {
            Text(
                "Retry"
            )
        }
    } else {
        when (videoGame) {
            null -> NotFound("VideoGame not found. id: $videoGameId")
            else -> VideoGameInformation(videoGame)
        }
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
                    model = "https:${videoGame.cover.url}",
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
                text = formatUnixTimestamp(videoGame.first_release_date),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            PlatformsComponent(videoGame)
            GenresComponent(videoGame)
            InvolvedCompaniesComponent(videoGame)
            Text(
                text = "Summary",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
            Text(
                text = videoGame.summary.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.contentPadding)
            )
        }
    }
}

@Composable
private fun InvolvedCompaniesComponent(videoGame: VideoGame) {
    Column {
        Text(
            text = "Involved Companies",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(AppSize.contentPadding)
        )
        videoGame.involved_companies.forEach { company ->
            Text(
                text = "• ${company.company.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.spacingLarge, 0.dp, 0.dp, AppSize.spacingMedium)
            )
        }
    }
}

@Composable
private fun GenresComponent(videoGame: VideoGame) {
    Column {
        Text(
            text = "Genres",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(AppSize.contentPadding)
        )
        videoGame.genres.forEach { genre ->
            Text(
                text = "• ${genre.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.spacingLarge, 0.dp, 0.dp, AppSize.spacingMedium)
            )
        }
    }
}

@Composable
private fun PlatformsComponent(videoGame: VideoGame) {
    Column {
        Text(
            text = "Platforms",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(AppSize.contentPadding)
        )
        videoGame.platforms.forEach { platform ->
            Text(
                text = "• ${platform.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.spacingLarge, 0.dp, 0.dp, AppSize.spacingMedium)
            )
        }
    }
}

private fun formatUnixTimestamp(unixTimestamp: Long): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
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
