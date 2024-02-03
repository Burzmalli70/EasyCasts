package dev.joewilliams.easycasts.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val mutableMainUiFlow: MutableStateFlow<MainUiState> = MutableStateFlow(
        MainUiState.PodcastList(emptyList())
    )
    val mainUiFlow: StateFlow<MainUiState> = mutableMainUiFlow

    fun switchScreen() {

    }
}