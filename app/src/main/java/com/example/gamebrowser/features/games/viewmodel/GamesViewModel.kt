package com.example.gamebrowser.features.games.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebrowser.data.repository.IGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val repository: IGameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GamesUiState>(GamesUiState.Loading)
    val uiState: StateFlow<GamesUiState> = _uiState

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            try {
                val games = repository.getGames(1)
                _uiState.value = GamesUiState.Success(games)
            } catch (e: Exception) {
                _uiState.value = GamesUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }
}
