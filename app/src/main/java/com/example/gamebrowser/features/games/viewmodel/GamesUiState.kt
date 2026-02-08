package com.example.gamebrowser.features.games.viewmodel

import com.example.gamebrowser.data.model.GameDto

sealed interface GamesUiState {
    object Loading : GamesUiState
    data class Success(val games: List<GameDto>) : GamesUiState
    data class Error(val message: String) : GamesUiState
}
