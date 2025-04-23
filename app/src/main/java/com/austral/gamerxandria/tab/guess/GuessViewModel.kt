package com.austral.gamerxandria.tab.guess

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
class GuessViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl
) : ViewModel() {
    private var _videoGame = MutableStateFlow<VideoGame?>(null)
    val videoGame = _videoGame.asStateFlow()

    private var _loading = MutableStateFlow(true)  // Start with loading state
    val loading = _loading.asStateFlow()

    private var _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

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
}
