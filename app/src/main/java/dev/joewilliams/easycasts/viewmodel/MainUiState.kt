package dev.joewilliams.easycasts.viewmodel

import dev.joewilliams.easycasts.model.Episode
import dev.joewilliams.easycasts.model.Podcast

sealed class MainUiState {
    var nowPlaying: Episode? = null
    var currentTimestampSeconds: Int? = null

    data class PodcastList(
        val podcasts: List<Podcast>
    ) : MainUiState()

    data class PodcastDetail(
        val podcast: Podcast
    ) : MainUiState()

    data class EpisodeDetail(
        val episode: Episode
    ) : MainUiState()

    data class Search(
        val query: String?
    ) : MainUiState()
}
