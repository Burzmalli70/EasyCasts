package dev.joewilliams.easycasts.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dev.joewilliams.easycasts.model.Podcast
import dev.joewilliams.easycasts.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun PodcastGrid(
    modifier: Modifier = Modifier,
    podcastListState: State<List<Podcast>>,
    podcastClicked: (Podcast) -> Unit
) {
    val podcasts by podcastListState
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(4)
    ) {
        items(podcastListState.value.size) {
            val podcast = podcasts[it]

            AsyncImage(
                modifier = Modifier.clickable {
                    podcastClicked.invoke(podcast)
                },
                model = podcast.largeImgUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}