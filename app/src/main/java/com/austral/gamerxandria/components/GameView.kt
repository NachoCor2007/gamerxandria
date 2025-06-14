package com.austral.gamerxandria.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.austral.gamerxandria.R
import com.austral.gamerxandria.storage.Shelf
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.tab.NotFound
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.ButtonRed
import com.austral.gamerxandria.ui.theme.CardBackground
import com.austral.gamerxandria.ui.theme.GameViewTitle
import com.austral.gamerxandria.ui.theme.InactiveTabColorLight
import com.austral.gamerxandria.ui.theme.TextWhite
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
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    } else if (showRetry) {
        Text(
            stringResource(R.string.guess_tab_could_not_load_game)
        )
        Button(
            onClick = { viewModel.retryApiCall() }
        ) {
            Text(
                stringResource(R.string.retry)
            )
        }
    } else {
        when (videoGame) {
            null -> NotFound(stringResource(R.string.game_view_id_not_found) + videoGameId)
            else -> VideoGameInformation(videoGame, viewModel)
        }
    }
}

@Composable
private fun VideoGameInformation(videoGame: VideoGame, viewModel: GameViewModel) {
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
                if (videoGame.cover != null && videoGame.cover.url != null) {
                    AsyncImage(
                        model = stringResource(R.string.game_shelf_image_url_prefix) + videoGame.cover.url,
                        contentDescription = stringResource(R.string.game_shelf_cover_message),
                        contentScale = ContentScale.Crop,
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
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

            ShelfManagementButton(videoGame, viewModel)

            if (videoGame.first_release_date != null) {
                Text(
                    text = formatUnixTimestamp(videoGame.first_release_date),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(AppSize.contentPadding)
                )
            }
            PlatformsComponent(videoGame)
            GenresComponent(videoGame)
            InvolvedCompaniesComponent(videoGame)
            Text(
                text = stringResource(R.string.game_view_summary),
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
private fun ShelfManagementButton(videoGame: VideoGame, viewModel: GameViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val shelves by viewModel.shelves.collectAsState(listOf())

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSize.contentPadding)
    ) {
        Text(stringResource(R.string.game_view_shelf_management_message))
    }

    if (showDialog) {
        ShelfManagementDialog(
            videoGame = videoGame,
            shelves = shelves,
            onDismiss = { showDialog = false },
            onSave = { updatedSelections ->
                viewModel.updateGameShelves(videoGame.id, updatedSelections)
                showDialog = false
            }
        )
    }
}

@Composable
private fun ShelfManagementDialog(
    videoGame: VideoGame,
    shelves: List<Shelf>,
    onDismiss: () -> Unit,
    onSave: (Map<String, Boolean>) -> Unit
) {
    val viewModel = hiltViewModel<GameViewModel>()
    val shelfSelections = remember {
        mutableStateOf(shelves.associate { shelf -> shelf.name to false })
    }

    val hasLoadedInitialSelections = remember { mutableStateOf(false) }

    LaunchedEffect(videoGame.id) {
        if (!hasLoadedInitialSelections.value) {
            val updatedSelections = mutableMapOf<String, Boolean>()

            shelves.forEach { shelf ->
                val isInShelf = viewModel.isGameInShelf(shelf.name, videoGame.id)
                updatedSelections[shelf.name] = isInShelf
            }

            shelfSelections.value = updatedSelections
            hasLoadedInitialSelections.value = true
        }
    }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss,
        title = { Text(style =
            MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.game_view_shelf_management_message)) },
        text = {
            Column {
                Text(
                    stringResource(R.string.game_view_video_game_shelves) + "\"${videoGame.name}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = AppSize.spacingMedium)
                )

                if (!hasLoadedInitialSelections.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    shelves.forEach { shelf ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = shelfSelections.value[shelf.name] == true,
                                onCheckedChange = { isChecked ->
                                    shelfSelections.value = shelfSelections.value.toMutableMap().apply {
                                        put(shelf.name, isChecked)
                                    }
                                }
                            )
                            Text(
                                text = shelf.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(shelfSelections.value) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonRed
                ),
                // Deshabilitar el botón hasta que las selecciones iniciales se hayan cargado
                enabled = hasLoadedInitialSelections.value
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun InvolvedCompaniesComponent(videoGame: VideoGame) {
    Column {
        Text(
            text = stringResource(R.string.game_view_involved_companies),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(AppSize.contentPadding)
        )
        videoGame.involved_companies?.forEach { company ->
            Text(
                text = stringResource(R.string.game_view_display_point_prefix) + company.company.name,
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
            text = stringResource(R.string.game_view_genres),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(AppSize.contentPadding)
        )
        videoGame.genres?.forEach { genre ->
            Text(
                text = stringResource(R.string.game_view_display_point_prefix) + genre.name,
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
            text = stringResource(R.string.game_view_platforms),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(AppSize.contentPadding)
        )
        videoGame.platforms?.forEach { platform ->
            Text(
                text = stringResource(R.string.game_view_display_point_prefix) + platform.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(AppSize.spacingLarge, 0.dp, 0.dp, AppSize.spacingMedium)
            )
        }
    }
}

private fun formatUnixTimestamp(unixTimestamp: Long): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
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
