package com.austral.gamerxandria.tab.guess

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.austral.gamerxandria.R
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun GuessTab() {
    val guesses = remember { mutableStateListOf<Guess>() }
    var currentInput = remember { mutableStateOf("") }
    var remainingTries = rememberSaveable { mutableIntStateOf(5) } // Set max tries

    Column(modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
        Image(
            painterResource(R.drawable.stand_by_image),
            contentDescription = "Stand ready for my arrival, worm",
        )

        Column(modifier = Modifier.padding(0.dp, 16.dp)) {
            TextField(
                value = currentInput.value,
                onValueChange = { currentInput.value = it },
                label = { Text("Enter your guess") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = remainingTries.intValue > 0 // Disable input if no tries left
            )

            if (guesses.any { it.status == GuessStatus.CORRECT }) {
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
                ) {
                    Text(
                        text = "Congratulations! You guessed the game!",
                    )
                }
            }

            Text(
                text = "Remaining tries: ${remainingTries.intValue}",
                modifier = Modifier.padding(16.dp),
                color = if (remainingTries.intValue > 0) Color.White else Color.Red
            )

            Button(
                onClick = {
                    if (currentInput.value.isNotBlank() && remainingTries.intValue > 0) {
                        guesses.add(Guess(currentInput.value, GuessStatus.WRONG)) // Default status
                        currentInput.value = "" // Clear input
                        remainingTries.intValue-- // Decrement tries
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                enabled = remainingTries.intValue > 0 // Disable button if no tries left
            ) {
                Text("Submit")
            }

            GuessList(guesses)
        }
    }
}

data class Guess(val text: String, val status: GuessStatus)

enum class GuessStatus {
    WRONG, NEAR, CORRECT
}

@Composable
fun GuessList(guesses: List<Guess>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        guesses.forEach { guess ->
            val backgroundColor = when (guess.status) {
                GuessStatus.WRONG -> Color(0xFFEC7C7C) // Red
                GuessStatus.NEAR -> Color(0xFFECD07C) // Yellow
                GuessStatus.CORRECT -> Color(0xFF8DEC7C) // Green
            }

            Text(
                text = guess.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    .background(backgroundColor)
                    .padding(16.dp),
                color = Color.Black
            )

            if (guess.status != GuessStatus.CORRECT) {
                Text(
                    text = "Hint",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                        .background(Color(0xFF493077))
                        .padding(8.dp),
                )
            }
        }
    }
}
