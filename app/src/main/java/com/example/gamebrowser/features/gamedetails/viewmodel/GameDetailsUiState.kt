package com.example.gamebrowser.features.gamedetails.viewmodel

import com.example.gamebrowser.data.model.dto.GameDto

sealed interface GameDetailsUiState {
    object Loading : GameDetailsUiState

    data class Success(
        val game: GameDto,
        val genresText: String,
        val platformsText: String,
        val releaseDateFormatted: String,
        val screenshots: List<String>,
        val trailerUrl: String? = null
    ) : GameDetailsUiState

    data class Error(val message: String) : GameDetailsUiState
}