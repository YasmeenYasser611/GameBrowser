package com.example.gamebrowser.features.games.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebrowser.data.repository.IGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val repository: IGameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = GamesUiState.Loading
            try {
                val games = repository.getGames(page)

                // Process data here - UI has NO logic
                val processedState = GamesUiState.Success(
                    allGames = games,
                    featuredGames = games.filter { it.rating >= 4.5 }.take(5),
                    newReleases = games.sortedByDescending { it.releaseDate }.take(10),
                    topRated = games.sortedByDescending { it.rating }.take(10),
                    popular = games.take(8)
                )

                _uiState.value = processedState
            } catch (e: Exception) {
                _uiState.value = GamesUiState.Error(
                    message = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun retry() {
        loadGames()
    }
}