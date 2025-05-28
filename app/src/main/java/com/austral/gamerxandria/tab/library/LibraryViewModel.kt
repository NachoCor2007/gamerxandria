package com.austral.gamerxandria.tab.library

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.gamerxandria.R
import com.austral.gamerxandria.mock.userShelvesMock
import com.austral.gamerxandria.model.Shelf
import com.austral.gamerxandria.security.BiometricAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val biometricAuthManager: BiometricAuthManager
) : ViewModel() {
//    Shelf related properties
    private var _shelves = MutableStateFlow(userShelvesMock)
    val shelves = _shelves.asStateFlow()
//    Auth related properties
    private var _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

//    Shelf related methods
    fun retrieveShelves(): List<Shelf> {
        return shelves.value
    }

    fun addShelf(shelfName: String) {
        val shelf = Shelf(shelfName, listOf())
        viewModelScope.launch {
            _shelves.emit(
                _shelves.value + shelf
            )
        }
    }

//    Auth related methods
    fun authenticate(context: Context) {
        biometricAuthManager.authenticate(
            context,
            onError = {
                _isAuthenticated.value = false
                Toast.makeText(context,
                    context.getString(R.string.authentication_on_error_message), Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                _isAuthenticated.value = true
            },
            onFail = {
                _isAuthenticated.value = false
                Toast.makeText(context,
                    context.getString(R.string.authentication_on_fail_message), Toast.LENGTH_SHORT).show()
            }
        )
    }
}
