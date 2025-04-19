package com.austral.gamerxandria.tab.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.gamerxandria.mock.userShelvesMock
import com.austral.gamerxandria.model.Shelf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor() : ViewModel() {
    private var _collections = MutableStateFlow(userShelvesMock)
    val collections = _collections.asStateFlow()

    fun retrieveShelves(): List<Shelf> {
        return collections.value
    }

    fun addShelf(shelfName: String) {
        val shelf = Shelf(shelfName, listOf())
        viewModelScope.launch {
            _collections.emit(
                _collections.value + shelf
            )
        }
    }
}
