package com.example.gamebrowser.features.games.viewmodel

import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GenreDto

sealed interface GamesUiState {
    object Loading : GamesUiState

    data class Success(
        val allGames: List<GameDto>,
        val filteredGames: List<GameDto>,
        val featuredGames: List<GameDto>,
        val newReleases: List<GameDto>,
        val topRated: List<GameDto>,
        val popular: List<GameDto>,
        val currentPage: Int,
        val hasMorePages: Boolean,
        val isLoadingMore: Boolean,
        val searchQuery: String,
        val selectedGenreId: String?,
        val genres: List<GenreDto>,
        val isEmpty: Boolean
    ) : GamesUiState

    data class Error(val message: String) : GamesUiState
}