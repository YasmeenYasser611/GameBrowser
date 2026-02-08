package com.example.gamebrowser.features.gamedetails.viewmodel


import androidx.lifecycle.SavedStateHandle
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
class GameDetailsViewModel @Inject constructor(
    private val repository: IGameRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameDetailsUiState>(GameDetailsUiState.Loading)
    val uiState: StateFlow<GameDetailsUiState> = _uiState.asStateFlow()

    private var gameId: Int? = null

    init {
        gameId = savedStateHandle.get<String>("gameId")?.toIntOrNull()
        if (gameId != null) {
            loadGameDetails(gameId!!)
        } else {
            _uiState.value = GameDetailsUiState.Error("Invalid game ID")
        }
    }

    private fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            _uiState.value = GameDetailsUiState.Loading
            try {
                val game = repository.getGameDetails(gameId)

                if (game != null) {
                    // Process all data in ViewModel
                    val genresText = formatGenres(game.genres?.map { it.name } ?: emptyList())
                    val platformsText = formatPlatforms(game.platforms?.map { it.platform.name } ?: emptyList())
                    val releaseDateFormatted = formatReleaseDate(game.releaseDate)

                    _uiState.value = GameDetailsUiState.Success(
                        game = game,
                        genresText = genresText,
                        platformsText = platformsText,
                        releaseDateFormatted = releaseDateFormatted
                    )
                } else {
                    _uiState.value = GameDetailsUiState.Error("Game not found")
                }
            } catch (e: Exception) {
                _uiState.value = GameDetailsUiState.Error(
                    message = e.message ?: "Failed to load game details"
                )
            }
        }
    }

    fun retry() {
        gameId?.let { id ->
            loadGameDetails(id)
        } ?: run {
            _uiState.value = GameDetailsUiState.Error("Invalid game ID")
        }
    }

    // PRIVATE HELPER METHODS - ALL LOGIC HERE

    private fun formatGenres(genres: List<String>): String {
        return if (genres.isEmpty()) {
            "No genres available"
        } else {
            genres.joinToString(", ")
        }
    }

    private fun formatPlatforms(platforms: List<String>): String {
        return if (platforms.isEmpty()) {
            "No platforms available"
        } else {
            platforms.take(5).joinToString(", ") +
                    if (platforms.size > 5) " and more" else ""
        }
    }

    private fun formatReleaseDate(releaseDate: String?): String {
        return releaseDate ?: "Release date unknown"
    }
}