package com.sakethh.jetspacer.explore.presentation

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen() {
    val exploreScreenViewModel: ExploreScreenViewModel = viewModel()
    Column {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (ExploreScreenViewModel.isSearchBarExpanded.value) 0.dp else 15.dp),
            inputField = {
                ProvideTextStyle(MaterialTheme.typography.titleSmall) {
                    SearchBarDefaults.InputField(
                        placeholder = {
                            Text("Search in NASA Image Library", modifier = Modifier.basicMarquee())
                        },
                        trailingIcon = {
                            if (ExploreScreenViewModel.isSearchBarExpanded.value) {
                                IconButton(onClick = {
                                    exploreScreenViewModel.querySearch.value = ""
                                    ExploreScreenViewModel.isSearchBarExpanded.value = false
                                }) {
                                    Icon(Icons.Filled.Clear, null)
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, null)
                        },
                        query = exploreScreenViewModel.querySearch.value,
                        onQueryChange = {
                            exploreScreenViewModel.querySearch.value = it
                        },
                        onSearch = {

                        },
                        expanded = ExploreScreenViewModel.isSearchBarExpanded.value,
                        onExpandedChange = {
                            ExploreScreenViewModel.isSearchBarExpanded.value = it
                        }
                    )
                }
            },
            expanded = ExploreScreenViewModel.isSearchBarExpanded.value,
            onExpandedChange = {
                ExploreScreenViewModel.isSearchBarExpanded.value = it
            }) {
        }
    }
}