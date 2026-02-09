package com.example.gamebrowser.features.games.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebrowser.data.model.dto.GameDto
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

    private val allLoadedGames = mutableListOf<GameDto>()
    private var currentPage = 1
    private var currentGenreId: String? = null
    private var isLastPage = false

    // Category data for navigation
    var categoryTitle by mutableStateOf("")
        private set
    var categoryGames by mutableStateOf<List<GameDto>>(emptyList())
        private set

    init {
        loadInitialData()
    }

    fun setCategoryData(title: String, games: List<GameDto>) {
        categoryTitle = title
        categoryGames = games
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = GamesUiState.Loading
            try {
                // Load genres first
                val genres = repository.getGenres()

                // Load first page of games
                val games = repository.getGames(page = 1)
                allLoadedGames.clear()
                allLoadedGames.addAll(games)
                currentPage = 1
                isLastPage = games.size < 20

                // Process and emit success state
                _uiState.value = createSuccessState(
                    allGames = allLoadedGames,
                    genres = genres,
                    searchQuery = "",
                    selectedGenreId = null,
                    isLoadingMore = false
                )
            } catch (e: Exception) {
                _uiState.value = GamesUiState.Error(
                    message = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val currentState = _uiState.value
        if (currentState is GamesUiState.Success) {
            // Filter already loaded games locally
            val filteredGames = filterGamesLocally(allLoadedGames, query)

            _uiState.value = currentState.copy(
                searchQuery = query,
                filteredGames = filteredGames,
                isEmpty = filteredGames.isEmpty()
            )
        }
    }

    fun onGenreSelected(genreId: String?) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is GamesUiState.Success) {
                // Show loading state for genre change
                _uiState.value = currentState.copy(isLoadingMore = true)

                try {
                    // Fetch games for selected genre
                    currentGenreId = genreId
                    currentPage = 1
                    val games = repository.getGames(
                        page = 1,
                        genreId = genreId
                    )

                    allLoadedGames.clear()
                    allLoadedGames.addAll(games)
                    isLastPage = games.size < 20

                    _uiState.value = createSuccessState(
                        allGames = allLoadedGames,
                        genres = currentState.genres,
                        searchQuery = currentState.searchQuery,
                        selectedGenreId = genreId,
                        isLoadingMore = false
                    )
                } catch (e: Exception) {
                    // Keep current state but stop loading
                    _uiState.value = currentState.copy(isLoadingMore = false)
                }
            }
        }
    }

    fun loadNextPage() {
        val currentState = _uiState.value
        if (currentState is GamesUiState.Success &&
            !currentState.isLoadingMore &&
            currentState.hasMorePages) {

            viewModelScope.launch {
                _uiState.value = currentState.copy(isLoadingMore = true)

                try {
                    val nextPage = currentPage + 1
                    val newGames = repository.getGames(
                        page = nextPage,
                        genreId = currentGenreId
                    )

                    if (newGames.isNotEmpty()) {
                        allLoadedGames.addAll(newGames)
                        currentPage = nextPage
                        isLastPage = newGames.size < 20

                        _uiState.value = createSuccessState(
                            allGames = allLoadedGames,
                            genres = currentState.genres,
                            searchQuery = currentState.searchQuery,
                            selectedGenreId = currentState.selectedGenreId,
                            isLoadingMore = false
                        )
                    } else {
                        isLastPage = true
                        _uiState.value = currentState.copy(
                            isLoadingMore = false,
                            hasMorePages = false
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = currentState.copy(isLoadingMore = false)
                }
            }
        }
    }

    fun retry() {
        loadInitialData()
    }

    // PRIVATE HELPER METHODS - ALL LOGIC HERE

    private fun createSuccessState(
        allGames: List<GameDto>,
        genres: List<com.example.gamebrowser.data.model.dto.GenreDto>,
        searchQuery: String,
        selectedGenreId: String?,
        isLoadingMore: Boolean
    ): GamesUiState.Success {
        // Apply search filter if there's a query
        val filteredGames = if (searchQuery.isNotEmpty()) {
            filterGamesLocally(allGames, searchQuery)
        } else {
            allGames
        }

        // Process games into different categories
        val featured = filteredGames.filter { it.rating >= 4.5 }.take(5)
        val newReleases = filteredGames.sortedByDescending { it.releaseDate }.take(10)
        val topRated = filteredGames.sortedByDescending { it.rating }.take(10)
        val popular = filteredGames.take(8)

        return GamesUiState.Success(
            allGames = allGames,
            filteredGames = filteredGames,
            featuredGames = featured,
            newReleases = newReleases,
            topRated = topRated,
            popular = popular,
            currentPage = currentPage,
            hasMorePages = !isLastPage,
            isLoadingMore = isLoadingMore,
            searchQuery = searchQuery,
            selectedGenreId = selectedGenreId,
            genres = genres,
            isEmpty = filteredGames.isEmpty()
        )
    }

    private fun filterGamesLocally(games: List<GameDto>, query: String): List<GameDto> {
        if (query.isBlank()) return games

        val lowercaseQuery = query.lowercase()
        return games.filter { game ->
            game.name.lowercase().contains(lowercaseQuery)
        }
    }
}