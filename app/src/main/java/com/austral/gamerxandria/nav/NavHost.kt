package com.austral.gamerxandria.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.austral.gamerxandria.tab.guess.GuessTab
import com.austral.gamerxandria.tab.library.LibraryTab
import com.austral.gamerxandria.tab.search.SearchTab
import com.austral.gamerxandria.tab.settings.SettingsTab

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = GamerxandriaNouns.Search.name,
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(20.dp)
    ) {
        composable(route = GamerxandriaNouns.Library.name) {
            LibraryTab()
        }
        composable(route = GamerxandriaNouns.Search.name) {
            SearchTab()
        }
        composable(route = GamerxandriaNouns.Guess.name) {
            GuessTab()
        }
        composable(route = GamerxandriaNouns.Settings.name) {
            SettingsTab()
        }
    }
}
