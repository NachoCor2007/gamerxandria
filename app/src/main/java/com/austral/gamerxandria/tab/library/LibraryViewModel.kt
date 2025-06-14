package com.austral.gamerxandria.tab.library

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.austral.gamerxandria.R
import com.austral.gamerxandria.storage.Shelf
import com.austral.gamerxandria.security.BiometricAuthManager
import com.austral.gamerxandria.storage.GamerxandriaDatabase
import com.austral.gamerxandria.storage.ShelfVideoGame
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val biometricAuthManager: BiometricAuthManager
) : ViewModel() {
    private val database = GamerxandriaDatabase.getDatabase(context)
    private val shelfDao = database.shelfDao()
    private val shelfVideoGameDao = database.videoGameIdDao()

    val shelves = shelfDao.getAllShelves().asFlow()

    private var _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    //    Shelf related methods
    fun addShelf(shelfName: String) {
        viewModelScope.launch {
            val existingShelf = shelfDao.getShelfByName(shelfName)
            if (existingShelf == null) {
                val shelf = Shelf(shelfName)
                shelfDao.insert(shelf)
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.already_existing_shelf_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun getVideoGamesInShelf(shelfName: String): LiveData<List<ShelfVideoGame>> {
        return shelfVideoGameDao.getVideoGameIdsByShelfName(shelfName)
    }

    fun deleteShelf(shelfName: String) {
        viewModelScope.launch {
            val shelf = shelfDao.getShelfByName(shelfName)
            if (shelf != null) {
                shelfDao.delete(shelf)
            }
        }
    }

    // Auth related methods
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
