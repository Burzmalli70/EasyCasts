package dev.joewilliams.easycasts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.joewilliams.easycasts.model.Episode
import dev.joewilliams.easycasts.model.Podcast
import dev.joewilliams.easycasts.network.PodcastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val podcastRepository = PodcastRepository()

    private val podcastsMutableStateFlow = MutableStateFlow(emptyList<Podcast>())
    private val subscribedPodcasts: StateFlow<List<Podcast>> = podcastsMutableStateFlow
    val subscribedPodcastsDirect = podcastRepository.subscribedPodcasts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val mutableSearchFlow: MutableStateFlow<MainUiState.Search> = MutableStateFlow(MainUiState.Search())
    val searchFlow: StateFlow<MainUiState.Search> = mutableSearchFlow

    private val mutableSelectedPodcast: MutableStateFlow<Podcast?> = MutableStateFlow(null)
    val selectedPodcast = mutableSelectedPodcast

    private val mutableSelectedEpisode: MutableStateFlow<Episode?> = MutableStateFlow(null)
    val selectedEpisode = mutableSelectedEpisode

    init {
        viewModelScope.launch(Dispatchers.IO) {
            podcastsMutableStateFlow.emit(podcastRepository.getPodcasts())
        }
    }

    fun updateSearch(query: String?) {
        viewModelScope.launch {
            mutableSearchFlow.emit(searchFlow.value.copy(query = query))
        }
    }

    fun searchPodcasts(query: String?) {
        query?.let {
            if (it.length >= 3) {
                viewModelScope.launch(Dispatchers.IO) {
                    val results = podcastRepository.searchPodcasts(query)
                    mutableSearchFlow.emit(searchFlow.value.copy(results = results))
                }
            }
        }
    }

    fun addRemovePodcast(podcast: Podcast) {
        viewModelScope.launch(Dispatchers.IO) {
            subscribedPodcasts.collectLatest {
                if (it.map { pod -> pod.sourceUri }.contains(podcast.sourceUri)) {
                    podcastRepository.removePodcast(podcast)
                } else {
                    podcastRepository.addPodcast(podcast)
                }
                subscribedPodcasts.collectLatest {  pods ->
                    podcastsMutableStateFlow.emit(pods)
                }
            }
        }
    }
}