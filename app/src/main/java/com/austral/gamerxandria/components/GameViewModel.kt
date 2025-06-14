package com.austral.gamerxandria.components

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.austral.gamerxandria.apiManager.ApiServiceImpl
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.storage.GamerxandriaDatabase
import com.austral.gamerxandria.storage.ShelfVideoGame
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val database = GamerxandriaDatabase.getDatabase(context)
    private val shelfDao = database.shelfDao()
    private val shelfVideoGameDao = database.videoGameIdDao()

    private var _videoGame = MutableStateFlow<VideoGame?>(null)
    val videoGame = _videoGame.asStateFlow()

    val shelves = shelfDao.getAllShelves().asFlow()

    private var _loading = MutableStateFlow(true)
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
            shelfSelections.forEach { (shelfName, isSelected) ->
                if (isSelected) {
                    val isInShelf = shelfVideoGameDao.isGameInShelf(shelfName, gameId)
                    if (!isInShelf) {
                        val shelfVideoGame = ShelfVideoGame(
                            shelfName = shelfName,
                            videoGameId = gameId
                        )
                        shelfVideoGameDao.insert(shelfVideoGame)

                        scheduleNotification(shelfName)
                    }
                } else {
                    shelfVideoGameDao.deleteVideoGameFromShelf(shelfName, gameId)
                }
            }
        }
    }

    suspend fun isGameInShelf(shelfName: String, gameId: Int): Boolean {
        return shelfVideoGameDao.isGameInShelf(shelfName, gameId)
    }

    fun getVideoGamesInShelf(shelfName: String): LiveData<List<ShelfVideoGame>> {
        return shelfVideoGameDao.getVideoGameIdsByShelfName(shelfName)
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(shelfName: String) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("shelfName", shelfName)
        }

        val requestCode = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val triggerTimeMillis = System.currentTimeMillis() + 1000L
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )
    }
}
