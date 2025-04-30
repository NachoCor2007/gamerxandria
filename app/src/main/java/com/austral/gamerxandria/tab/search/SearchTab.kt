package com.austral.gamerxandria.tab.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.gamerxandria.R
import com.austral.gamerxandria.components.GameCard
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.tab.NotFound
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.CardBackground

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchTab(navigateToGameView: (Int) -> Unit) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val videoGames = viewModel.videoGames.collectAsStateWithLifecycle().value
    val loading = viewModel.loading.collectAsStateWithLifecycle().value
    val showRetry = viewModel.showRetry.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SimpleSearchBar(
            onSearch = { query ->
                viewModel.searchVideoGamesNames(query)
                       },
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSize.contentPadding)
        )

        if (loading) {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        } else if (showRetry) {
            Text(
                stringResource(R.string.search_tab_error_message)
            )
        } else {
            when (videoGames) {
                emptyList<VideoGame>() -> NotFound(stringResource(R.string.search_tab_no_games_found))
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            FlowRow { videoGames.forEach { shelf -> GameCard(navigateToGameView, shelf) } }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    // Controls expansion state of the search bar
    var expanded = false

    Box(
        modifier
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            colors = SearchBarDefaults.colors(
                containerColor = CardBackground,
                dividerColor = CardBackground
            ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = {
                        onSearch(it)
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(R.string.search_bar_placeholder)) }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {}
    }
}
