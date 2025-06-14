package com.austral.gamerxandria.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.austral.gamerxandria.R
import com.austral.gamerxandria.storage.Shelf
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.GameCardTitle
import com.austral.gamerxandria.ui.theme.GameShelfTitle
import com.austral.gamerxandria.ui.theme.TextWhite
import com.austral.gamerxandria.ui.theme.CardBackground
import com.austral.gamerxandria.ui.theme.InactiveTabColorLight

@Composable
fun GameShelf(navigateToGameView: (Int) -> Unit, shelf: Shelf) {
    val viewModel = hiltViewModel<GameShelfViewModel>()

    LaunchedEffect(shelf.name) {
        viewModel.loadGamesFromDatabase(shelf.name)
    }

    val videoGames = viewModel.videoGames.collectAsStateWithLifecycle().value
    val loading = viewModel.loading.collectAsStateWithLifecycle().value
    val showRetry = viewModel.showRetry.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSize.noSpace, AppSize.spacingSmall)
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = TextWhite,
                modifier = Modifier.size(AppSize.spacingXXLarge)
            )
        } else if (showRetry) {
            Text(
                stringResource(R.string.game_shelf_could_not_load_video_games)
            )
            Button(
                onClick = {
                    viewModel.retryApiCall(shelf.name)
                }
            ) {
                Text(
                    stringResource(R.string.retry)
                )
            }
        } else {
            ShelfDisplay(shelf, videoGames[shelf.name], navigateToGameView)
        }
    }
}

@Composable
fun ShelfDisplay(
    shelf: Shelf,
    videoGames: List<VideoGame>?,
    navigateToGameView: (Int) -> Unit
) {
    Text(
        text = shelf.name,
        style = GameShelfTitle,
        modifier = Modifier.padding(bottom = AppSize.spacingSmall)
    )
    LazyRow {
        item {
            if (videoGames.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(AppSize.spacingTiny)
                        .height(AppSize.gameCardSize)
                        .width(AppSize.gameCardSize * 2)
                        .background(CardBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.game_shelf_empty_message),
                        style = GameCardTitle,
                        modifier = Modifier.padding(AppSize.spacingTiny),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                videoGames.forEach { videoGame ->
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
            .padding(AppSize.spacingTiny)
            .size(AppSize.gameCardSize),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (videoGame.cover != null) {
                AsyncImage(
                    model = stringResource(R.string.game_shelf_image_url_prefix) + videoGame.cover.url,
                    contentDescription = stringResource(R.string.game_shelf_cover_message),
                    contentScale = ContentScale.Crop,  // This ensures the image covers the whole area
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(Modifier
                    .background(InactiveTabColorLight)
                    .fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = stringResource(R.string.game_shelf_no_cover),
                        tint = TextWhite,
                        modifier = Modifier.size(AppSize.spacingXXXLarge)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                androidx.compose.ui.graphics.Color.Transparent,
                                androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,  // Top of the box
                            endY = Float.POSITIVE_INFINITY  // Bottom of the box
                        )
                    )
            )

            Text(
                text = videoGame.name,
                style = GameCardTitle,
                color = TextWhite,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(AppSize.contentPadding)
            )
        }
    }
}
