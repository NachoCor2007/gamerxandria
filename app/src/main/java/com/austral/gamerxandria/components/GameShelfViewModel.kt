package com.austral.gamerxandria.components

import androidx.lifecycle.ViewModel
import com.austral.gamerxandria.mock.VideoGameMock
import com.austral.gamerxandria.model.VideoGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GameShelfViewModel @Inject constructor() : ViewModel()  {
    private var _videoGames = MutableStateFlow(VideoGameMock)
    val videoGames = _videoGames.asStateFlow()

    fun retrieveVideoGamesByIds(ids: List<Int>): List<VideoGame> {
        return videoGames.value.filter { it.id in ids }
    }
}
