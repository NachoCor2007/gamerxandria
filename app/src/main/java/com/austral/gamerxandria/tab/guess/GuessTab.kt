package com.austral.gamerxandria.tab.guess

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.austral.gamerxandria.ui.theme.CardBackgroundDark
import com.austral.gamerxandria.ui.theme.InactiveTabColorLight
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
            else -> GuessingGame(viewModel, videoGame)
        }
    }
}

@Composable
private fun GuessingGame(viewModel: GuessViewModel, videoGame: VideoGame) {
    val guesses = remember { mutableStateListOf<Guess>() }
    var currentInput = remember { mutableStateOf("") }
    var remainingTries = rememberSaveable { mutableIntStateOf(5) } // Set max tries

    val searchResults = viewModel.searchResults.collectAsStateWithLifecycle().value

    Column {
        if (videoGame.cover != null) {
            AsyncImage(
                model = "https:${videoGame.cover.url}",
                contentDescription = "VideoGame cover",
                alignment = Alignment.Center,
                modifier = Modifier
                    .height(256.dp)
                    .fillMaxWidth()
            )
        } else {
            Box(Modifier
                .background(InactiveTabColorLight)
                .height(256.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = "Opened menu",
                    tint = TextWhite,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        Text(
            text = "Remaining tries: ${remainingTries.intValue}",
            modifier = Modifier.padding(AppSize.contentPadding),
            color = if (remainingTries.intValue > 0) TextWhite else StatusWrong,
            style = MaterialTheme.typography.bodyMedium
        )

        Column(modifier = Modifier.padding(0.dp, AppSize.contentPadding)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                TextField(
                    singleLine = true,
                    value = currentInput.value,
                    onValueChange = {
                        currentInput.value = it
                        viewModel.searchGames(it) // Trigger search on text change
                    },
                    label = { Text("Enter your guess") },
                    modifier = Modifier
                        .width(250.dp)
                        .padding(AppSize.contentPadding),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Button(
                    onClick = {
                        if (currentInput.value.isNotBlank() && remainingTries.intValue > 0) {
                            val guessValue = currentInput.value.lowercase()
                            val trueValue = videoGame.name.lowercase()

                            val guessStatus = if (guessValue == trueValue) {
                                GuessStatus.CORRECT
                            } else {
                                val similarity = similarity(guessValue, trueValue)

                                if (similarity >= 0.5) {
                                    GuessStatus.NEAR
                                } else {
                                    GuessStatus.WRONG
                                }
                            }

                            guesses.add(
                                Guess(
                                    currentInput.value,
                                    guessStatus
                                )
                            )

                            currentInput.value = "" // Clear input
                            remainingTries.intValue-- // Decrement tries
                            viewModel.flushSearchResults() // Clear search results
                        }
                    },
                    modifier = Modifier
                        .padding(AppSize.contentPadding),
                    enabled = remainingTries.intValue > 0, // Disable button if no tries left
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonRed,
                        disabledContainerColor = ButtonRed.copy(alpha = 0.6f)
                    )
                ) {
                    Text("Submit")
                }
            }

            // Display search results
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .background(CardBackgroundDark)
            ) {
                item {
                    searchResults.forEach { result ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 0.dp, 0.dp, AppSize.spacingSmall)
                                .padding(AppSize.contentPadding)
                                .clickable(true, onClick = {
                                    currentInput.value = result.name
                                    viewModel.searchGames(result.name) // Trigger search on text change
                                })
                        ) {
                            Text(
                                text = result.name,
                                color = TextWhite,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }

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

            GuessList(guesses, videoGame)
        }
    }
}

fun similarity(str1: String, str2: String): Double {
    val maxLength = maxOf(str1.length, str2.length)
    return if (maxLength == 0) 1.0 else 1.0 - (levenshteinDistance(str1, str2).toDouble() / maxLength)
}

fun levenshteinDistance(str1: String, str2: String): Int {
    val dp = Array(str1.length + 1) { IntArray(str2.length + 1) }

    for (i in 0..str1.length) {
        for (j in 0..str2.length) {
            if (i == 0) {
                dp[i][j] = j
            } else if (j == 0) {
                dp[i][j] = i
            } else {
                val cost = if (str1[i - 1] == str2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // Deletion
                    dp[i][j - 1] + 1,      // Insertion
                    dp[i - 1][j - 1] + cost // Substitution
                )
            }
        }
    }
    return dp[str1.length][str2.length]
}

data class Guess(val text: String, val status: GuessStatus)

enum class GuessStatus {
    WRONG, NEAR, CORRECT
}

@Composable
fun GuessList(guesses: List<Guess>, videoGame: VideoGame) {
    val hints = listOf(
        "Hint 1: The genre is ${videoGame.genres?.joinToString { it.name }}",
        "Hint 2: The game is available on ${videoGame.platforms?.joinToString { it.name }}",
        "Hint 3: Developed by ${videoGame.involved_companies?.joinToString { it.company.name }}",
        "Hint 4: The game was released in ${
            videoGame.first_release_date?.let { formatUnixTimestamp(it) }?.split(" ")[0]}",
        "Better luck next time!"
    )

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            var currentHintIndex = hints.size - 1 // Start from the last hint

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
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (guess.status != GuessStatus.CORRECT && currentHintIndex >= 0) {
                    Text(
                        text = hints[guesses.indexOf(guess)],
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
