package com.austral.gamerxandria.components

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.austral.gamerxandria.apiManager.ApiServiceImpl
import com.austral.gamerxandria.mock.userShelvesMock
import com.austral.gamerxandria.model.VideoGame
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.austral.gamerxandria.notification.NotificationReceiver
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl,
    private val savedStateHandle: SavedStateHandle  // Add this parameter
) : ViewModel() {
    private var _videoGame = MutableStateFlow<VideoGame?>(null)
    val videoGame = _videoGame.asStateFlow()

    private var _shelves = MutableStateFlow(userShelvesMock)
    val shelves = _shelves.asStateFlow()

    private var _loading = MutableStateFlow(true)  // Start with loading state
    val loading = _loading.asStateFlow()

    private var _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {
        // Extract the ID from the navigation arguments
        val videoGameId = savedStateHandle.get<String>("videoGameId")?.toIntOrNull()
        if (videoGameId != null) {
            loadVideoGame(videoGameId)
        } else {
            _loading.value = false
        }
    }

    fun retryApiCall() {
        val videoGameId = savedStateHandle.get<String>("videoGameId")?.toIntOrNull()
        if (videoGameId != null) {
            loadVideoGame(videoGameId)
        }
    }

    private fun loadVideoGame(id: Int) {
        _loading.value = true

        apiServiceImpl.getVideoGameById(
            id = id,
            context = context,
            onSuccess = { game ->
                _videoGame.value = game
                _showRetry.value = false
            },
            onFail = {
                _showRetry.value = true
            },
            loadingFinished = {
                _loading.value = false
            }
        )
    }

    fun updateGameShelves(gameId: Int, shelfSelections: Map<String, Boolean>) {
        viewModelScope.launch {
            val currentShelves = _shelves.value.toMutableList()
            val updatedShelves = currentShelves.map { shelf ->
                // Handle the shelf update based on selection status
                if (shelfSelections[shelf.name] == true) {
                    // Add game to shelf if not already present
                    if (!shelf.games.any { it == gameId }) {
                        // We need the full game object to add
                        val gameToAdd = _videoGame.value
                        if (gameToAdd != null) {
                            val updatedGames = shelf.games.toMutableList()
                            updatedGames.add(gameToAdd.id)

                            scheduleNotification(shelf.name)

                            shelf.copy(games = updatedGames)
                        } else {
                            shelf
                        }
                    } else {
                        shelf
                    }
                } else {
                    // Remove game from shelf if present
                    val updatedGames = shelf.games.filterNot { it == gameId }
                    shelf.copy(games = updatedGames)
                }
            }

            _shelves.value = updatedShelves
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(shelfName: String) {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("shelfName", shelfName) // Pass the shelf name to the intent
        }

        val requestCode = System.currentTimeMillis().toInt()

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get the selected time and schedule the notification
        val triggerTimeMillis = 0L
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )
    }
}
