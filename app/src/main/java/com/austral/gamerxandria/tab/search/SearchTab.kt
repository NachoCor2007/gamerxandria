package com.austral.gamerxandria.tab.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.gamerxandria.components.GameCard
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.CardBackground

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchTab(navigateToGameView: (Int) -> Unit) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val videoGames = viewModel.videoGames.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SimpleSearchBar(
            onQueryChange = { query ->
                viewModel.searchVideoGamesNames(query)
                       },
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSize.contentPadding)
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    onQueryChange: (String) -> Unit,
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
                        onQueryChange(it)
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {}
    }
}
