package com.austral.gamerxandria.components

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.gamerxandria.apiManager.ApiServiceImpl
import com.austral.gamerxandria.model.VideoGame
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameShelfViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl,
) : ViewModel() {
    private var _videoGames = MutableStateFlow(listOf<VideoGame>())
    val videoGames = _videoGames.asStateFlow()

    private var _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    fun retryApiCall(listOfVideoGamesIds: List<Int>) {
        loadGames(listOfVideoGamesIds)
    }

    fun loadGames(listOfVideoGamesIds: List<Int>) {
        _loading.value = true

        apiServiceImpl.getVideoGamesByIds(
            listOfVideoGamesIds,
            context = context,
            onSuccess = {
                viewModelScope.launch {
                    _videoGames.emit(it)
                }

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
}
