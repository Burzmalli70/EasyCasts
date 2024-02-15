package dev.joewilliams.easycasts.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun PodcastDetail(
    modifier: Modifier = Modifier
) {

}

@Composable
fun PodcastPreviewCard(
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
fun PodcastPreviewCardPreview() {
    PodcastPreviewCard(
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