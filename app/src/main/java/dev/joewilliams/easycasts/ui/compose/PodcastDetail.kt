package dev.joewilliams.easycasts.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.joewilliams.easycasts.R
import dev.joewilliams.easycasts.model.Episode
import dev.joewilliams.easycasts.model.Podcast
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PodcastDetail(
    modifier: Modifier = Modifier,
    podcastState: StateFlow<Podcast?>,
    episodeState: StateFlow<Episode?>
) {
    val episode by episodeState.collectAsState()
    val podcast by podcastState.collectAsState()
    episode?.let { ep ->
        EpisodeDetail(modifier = modifier, episode = ep)
    } ?: podcast?.let {

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
    episode: Episode
) {

}
