@file:OptIn(ExperimentalMaterial3Api::class)

package dev.joewilliams.easycasts.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.joewilliams.easycasts.model.Podcast
import dev.joewilliams.easycasts.viewmodel.MainUiState

@Composable
fun Search(
    modifier: Modifier = Modifier,
    searchState: State<MainUiState.Search>,
    podcastListState: State<List<Podcast>>,
    queryChanged: (String?) -> Unit,
    doSearch: (String?) -> Unit,
    addRemovePodcast: (Podcast) -> Unit,
    showPodcastDetail: (Podcast) -> Unit
) {
    val searchResults by searchState
    val podcasts by podcastListState
    val uriMap = podcasts.map { pod -> pod.sourceUri ?: "" }
    SearchBar(
        modifier = modifier,
        query = searchResults.query ?: "",
        onQueryChange = { queryChanged.invoke(it) },
        onSearch = { doSearch.invoke(it) },
        active = true,
        onActiveChange = {}
    ) {
        LazyColumn {
            items(searchResults.results?.size ?: 0) {
                val result = searchResults.results?.get(it) ?: return@items
                PodcastPreviewCard(
                    modifier = Modifier.fillMaxWidth(),
                    podcast = result,
                    isSubscribed = uriMap.contains(result.sourceUri),
                    onSubscribeButtonClicked = { addRemovePodcast.invoke(result) }
                ) {
                    showPodcastDetail(result)
                }
            }
        }
    }
}