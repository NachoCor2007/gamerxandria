package com.austral.gamerxandria.tab.guess

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.tab.NotFound
import com.austral.gamerxandria.ui.theme.AccentPurple
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.ButtonRed
import com.austral.gamerxandria.ui.theme.StatusCorrect
import com.austral.gamerxandria.ui.theme.StatusNear
import com.austral.gamerxandria.ui.theme.StatusWrong
import com.austral.gamerxandria.ui.theme.TextWhite
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun GuessTab() {
    val viewModel = hiltViewModel<GuessViewModel>()

    LaunchedEffect(1022) {
        viewModel.loadVideoGame(1022)
    }

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
            onClick = { viewModel.loadVideoGame(1022) }
        ) {
            Text(
                "Retry"
            )
        }
    } else {
        when (videoGame) {
            null -> NotFound("Could not generate guessing game. Try again later.")
            else -> GuessingGame(videoGame)
        }
    }
}

@Composable
private fun GuessingGame(videoGame: VideoGame) {
    val guesses = remember { mutableStateListOf<Guess>() }
    var currentInput = remember { mutableStateOf("") }
    var remainingTries = rememberSaveable { mutableIntStateOf(5) } // Set max tries

    Column(modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
        AsyncImage(
            model = "https:${videoGame.cover.url}",
            contentDescription = "VideoGame cover",
            alignment = Alignment.Center,
            modifier = Modifier.height(256.dp).fillMaxWidth()
        )

        Column(modifier = Modifier.padding(0.dp, AppSize.contentPadding)) {
            TextField(
                value = currentInput.value,
                onValueChange = { currentInput.value = it },
                label = { Text("Enter your guess") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSize.contentPadding),
                enabled = remainingTries.intValue > 0, // Disable input if no tries left
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                )
            )

            if (guesses.any { it.status == GuessStatus.CORRECT }) {
                remainingTries.intValue = 0

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(AppSize.contentPadding),
                ) {
                    Text(
                        text = "Congratulations! You guessed the game!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Text(
                text = "Remaining tries: ${remainingTries.intValue}",
                modifier = Modifier.padding(AppSize.contentPadding),
                color = if (remainingTries.intValue > 0) TextWhite else StatusWrong,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    if (currentInput.value.isNotBlank() && remainingTries.intValue > 0) {
                        guesses.add(Guess(currentInput.value,
                            if (currentInput.value.lowercase().equals(videoGame.name.lowercase())) { GuessStatus.CORRECT } else { GuessStatus.WRONG })) // Default status
                        currentInput.value = "" // Clear input
                        remainingTries.intValue-- // Decrement tries
                    }
                },
                modifier = Modifier
                    .padding(AppSize.contentPadding)
                    .align(Alignment.CenterHorizontally),
                enabled = remainingTries.intValue > 0, // Disable button if no tries left
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonRed,
                    disabledContainerColor = ButtonRed.copy(alpha = 0.6f)
                )
            ) {
                Text("Submit")
            }

            GuessList(guesses, videoGame)
        }
    }
}

data class Guess(val text: String, val status: GuessStatus)

enum class GuessStatus {
    WRONG, NEAR, CORRECT
}

@Composable
fun GuessList(guesses: List<Guess>, videoGame: VideoGame) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            guesses.asReversed().forEach { guess ->
                val backgroundColor = when (guess.status) {
                    GuessStatus.WRONG -> StatusWrong
                    GuessStatus.NEAR -> StatusNear
                    GuessStatus.CORRECT -> StatusCorrect
                }

                Text(
                    text = guess.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, AppSize.spacingSmall)
                        .background(backgroundColor)
                        .padding(AppSize.contentPadding),
                    color = androidx.compose.ui.graphics.Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (guess.status != GuessStatus.CORRECT) {
                    Text(
                        text = formatUnixTimestamp(videoGame.first_release_date),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 0.dp, AppSize.spacingSmall)
                            .background(AccentPurple)
                            .padding(AppSize.spacingSmall),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun formatUnixTimestamp(unixTimestamp: Long): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}
