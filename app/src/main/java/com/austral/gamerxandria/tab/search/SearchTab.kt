package com.austral.gamerxandria.tab.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.austral.gamerxandria.components.GameShelf

@Composable
fun SearchTab(navigateToGameView: () -> Unit) {
    val modelView = hiltViewModel<SearchViewModel>()

    Column(
        modifier = Modifier.fillMaxSize()
//            .verticalScroll(rememberScrollState())
    ) {
        SimpleSearchBar(
            textFieldState = TextFieldState(),
            onSearch = { /* Handle search */ },
            searchResults = listOf("Result 1", "Result 2", "Result 3"),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        LazyColumn {
            val shelves = modelView.retrieveShelves()

            item {
                shelves.forEach { shelf -> GameShelf(navigateToGameView, shelf.name) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
//    var expanded by rememberSaveable { mutableStateOf(false) }
    var expanded = false

    Box(
        modifier
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Display search results in a scrollable column
            Column(Modifier
//                .verticalScroll(rememberScrollState())
            ) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, result) }
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
