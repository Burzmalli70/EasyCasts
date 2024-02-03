package dev.joewilliams.easycasts.ui.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.joewilliams.easycasts.R

@Composable
fun EpisodeDetail(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.episodes_tab)
    )
}