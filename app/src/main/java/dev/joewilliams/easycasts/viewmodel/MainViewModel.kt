package dev.joewilliams.easycasts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.joewilliams.easycasts.network.PodcastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val podcastRepository = PodcastRepository()

    private val mutableMainUiFlow: MutableStateFlow<MainUiState> = MutableStateFlow(
        MainUiState.PodcastList(emptyList())
    )
    val mainUiFlow: StateFlow<MainUiState> = mutableMainUiFlow

    private val mutableSearchFlow: MutableStateFlow<MainUiState.Search> = MutableStateFlow(MainUiState.Search())
    val searchFlow: StateFlow<MainUiState.Search> = mutableSearchFlow

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
}