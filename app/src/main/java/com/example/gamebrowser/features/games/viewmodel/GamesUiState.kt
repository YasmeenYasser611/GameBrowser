package com.example.gamebrowser.features.games.viewmodel

import com.example.gamebrowser.data.model.GameDto

data class GamesUiState(
    val isLoading: Boolean = false,
    val games: List<GameDto> = emptyList(),
    val error: String? = null
)
