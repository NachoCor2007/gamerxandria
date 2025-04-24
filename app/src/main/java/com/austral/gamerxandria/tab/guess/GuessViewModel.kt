package com.austral.gamerxandria.tab.guess

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.gamerxandria.apiManager.ApiServiceImpl
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.model.VideoGameName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl
) : ViewModel() {
    private var _videoGame = MutableStateFlow<VideoGame?>(null)
    val videoGame = _videoGame.asStateFlow()

    private var _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {
        loadVideoGame(1022)
    }

    fun loadVideoGame(videoGameId: Int) {
        _loading.value = true

        apiServiceImpl.getVideoGameById(
            videoGameId,
            context = context,
            onSuccess = {
                viewModelScope.launch {
                    _videoGame.emit(it)
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

    private var _searchResults = MutableStateFlow<List<VideoGameName>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun flushSearchResults() {
        _searchResults.value = emptyList()
    }

    fun searchGames(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        apiServiceImpl.searchVideoGamesNames(
            guessedName = query,
            context = context,
            onSuccess = { results ->
                viewModelScope.launch {
                    _searchResults.emit(results)
                }
            },
            onFail = {
                _searchResults.value = emptyList()
            },
            loadingFinished = { /* Optional loading state */ }
        )
    }
}
