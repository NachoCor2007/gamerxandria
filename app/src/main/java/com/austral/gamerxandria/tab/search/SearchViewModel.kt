package com.austral.gamerxandria.tab.search

import android.content.Context
import androidx.lifecycle.ViewModel
import com.austral.gamerxandria.apiManager.ApiServiceImpl
import com.austral.gamerxandria.model.VideoGame
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl
) : ViewModel() {
    private var _videoGames = MutableStateFlow(listOf<VideoGame>())
    val videoGames = _videoGames.asStateFlow()

    private var _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {
        searchVideoGamesNames("")
    }

    fun searchVideoGamesNames(query: String) {
        _loading.value = true

        apiServiceImpl.searchVideoGamesByName(
            name = query,
            context = context,
            onSuccess = {
                _videoGames.value = it
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
