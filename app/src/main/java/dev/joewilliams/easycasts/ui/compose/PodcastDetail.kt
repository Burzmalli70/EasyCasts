package dev.joewilliams.easycasts.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.joewilliams.easycasts.R
import dev.joewilliams.easycasts.model.Episode
import dev.joewilliams.easycasts.model.Podcast
import dev.joewilliams.easycasts.viewmodel.MainViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Composable
fun PodcastDetail(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    podcastState: StateFlow<Podcast?>,
    episodeState: StateFlow<Episode?>
) {
    val coroutineScope = rememberCoroutineScope()

    val episode by episodeState.collectAsState()
    val podcast by podcastState.collectAsState()
    val episodeList by viewModel.episodeList.collectAsState()

    podcast?.let {
        coroutineScope.launch {
            viewModel.fetchPodcastEpisodes(it)
        }
    }

    episode?.let { ep ->
        EpisodeDetail(modifier = modifier, episode = ep)
    } ?: podcast?.let {
        LazyColumn {
            episodeList?.let {
                items(it) { ep ->
                    EpisodeListContent(episode = ep) {

                    }
                }
            }
        }
    } ?: Text(
        text = stringResource(id = R.string.invalid_podcast)
    )
}

@Composable
fun EpisodeDetail(
    modifier: Modifier = Modifier,
    episode: Episode
) {
    ConstraintLayout(
        modifier = modifier.padding(16.dp)
    ) {
        val (title, body) = createRefs()
        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = episode.title
        )
        Text(
            modifier = Modifier.constrainAs(body) {
                top.linkTo(title.bottom, 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = episode.description
        )
    }
}

@Composable
fun EpisodeListContent(
    modifier: Modifier = Modifier,
    episode: Episode,
    episodeClicked: () -> Unit
) {
//    ConstraintLayout(
//        modifier = modifier.clickable { episodeClicked.invoke() }
//    ) {
//
//    }
    Text(
        modifier = modifier.clickable { episodeClicked.invoke() },
        text = episode.title
    )
}
