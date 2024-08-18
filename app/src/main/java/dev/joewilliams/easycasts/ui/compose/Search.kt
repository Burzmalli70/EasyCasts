@file:OptIn(ExperimentalMaterial3Api::class)

package dev.joewilliams.easycasts.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import dev.joewilliams.easycasts.R
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
                SearchResultContent(
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

@Composable
fun SearchResultContent(
    modifier: Modifier = Modifier,
    podcast: Podcast,
    isSubscribed: Boolean,
    onSubscribeButtonClicked: () -> Unit,
    onPodcastClicked: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier.clickable { onPodcastClicked.invoke() }
    ) {
        val (image, title, blurb, subBtn) = createRefs()

        AsyncImage(
            modifier = Modifier
                .size(74.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            model = podcast.smallImgUrl,
            contentDescription = stringResource(id = R.string.podcast_image_content),
            placeholder = previewPlaceholder(id = R.mipmap.pod_img_placeholder)
        )

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                bottom.linkTo(blurb.top)
                start.linkTo(image.end, 16.dp)
                end.linkTo(parent.end, 16.dp)
                width = Dimension.fillToConstraints
            },
            text = podcast.name
        )

        Text(
            modifier = Modifier.constrainAs(blurb) {
                top.linkTo(title.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(image.end, 16.dp)
                end.linkTo(parent.end, 16.dp)
                width = Dimension.fillToConstraints
            },
            text = podcast.description,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        val startBarrier = createStartBarrier(blurb, title)
        val iconId = if (isSubscribed) R.drawable.ic_unsubscribe else R.drawable.ic_add_circle
        Icon(
            modifier = Modifier.constrainAs(subBtn) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(startBarrier)
            }.clickable { onSubscribeButtonClicked.invoke() },
            painter = painterResource(id = iconId),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultContentPreview() {
    SearchResultContent(
        podcast = Podcast(
            id = 12345,
            name = "Test Podcast",
            description = "A description for showing how a long-ish podcast description will appear in the preview card",
            smallImgUrl = null,
            largeImgUrl = null,
            sourceUri = ""
        ),
        isSubscribed = false,
        onPodcastClicked = {},
        onSubscribeButtonClicked = {}
    )
}