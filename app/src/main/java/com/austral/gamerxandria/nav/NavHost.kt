package com.austral.gamerxandria.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.austral.gamerxandria.R
import com.austral.gamerxandria.components.GameView
import com.austral.gamerxandria.tab.NotFound
import com.austral.gamerxandria.tab.guess.GuessTab
import com.austral.gamerxandria.tab.library.LibraryTab
import com.austral.gamerxandria.tab.profile.ProfileTab
import com.austral.gamerxandria.tab.search.SearchTab
import com.austral.gamerxandria.ui.theme.AppSize

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = GamerxandriaNouns.Library.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(AppSize.screenHorizontalPadding, 0.dp)
    ) {
        var navigateToGameView: (Int) -> Unit = { videoGameId ->
            navController.navigate("${GamerxandriaNouns.Game.name}/${videoGameId}")
        }

//      Tab routes.
        composable(route = GamerxandriaNouns.Library.name) {
            LibraryTab(navigateToGameView)
        }
        composable(route = GamerxandriaNouns.Search.name) {
            SearchTab(navigateToGameView)
        }
        composable(route = GamerxandriaNouns.Guess.name) {
            GuessTab()
        }
        composable(route = GamerxandriaNouns.Profile.name) {
            ProfileTab()
        }

//      Game display route.
        composable(route = "${GamerxandriaNouns.Game.name}/{videoGameId}") { backStackEntry ->
            val videoGameId: String? = backStackEntry.arguments?.getString("videoGameId")

            when (videoGameId) {
                null -> NotFound(stringResource(R.string.nav_host_video_game_null)) // Handle null case
                else -> GameView(videoGameId.toInt()) // Handle valid case
            }
        }
    }
}
