package com.austral.gamerxandria.tab.search

import androidx.lifecycle.ViewModel
import com.austral.gamerxandria.mock.searchShelvesMock
import com.austral.gamerxandria.model.Shelf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private var _shelves = MutableStateFlow(searchShelvesMock)
    val shelves = _shelves.asStateFlow()

    fun retrieveShelves(): List<Shelf> {
        return shelves.value
    }
}
