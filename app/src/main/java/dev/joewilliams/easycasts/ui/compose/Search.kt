@file:OptIn(ExperimentalMaterial3Api::class)

package dev.joewilliams.easycasts.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.joewilliams.easycasts.viewmodel.MainViewModel

@Composable
fun Search(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val searchState by viewModel.searchFlow.collectAsState()
    SearchBar(
        modifier = modifier,
        query = searchState.query ?: "",
        onQueryChange = { viewModel.updateSearch(it) },
        onSearch = { viewModel.searchPodcasts(it) },
        active = true,
        onActiveChange = {}
    ) {
        LazyColumn {
            items(searchState.results?.size ?: 0) {
                val result = searchState.results?.get(it) ?: return@items
                PodcastPreviewCard(
                    modifier = Modifier.fillMaxWidth(),
                    podcast = result
                )
            }
        }
    }
}