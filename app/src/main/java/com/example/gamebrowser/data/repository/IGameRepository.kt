package com.example.gamebrowser.data.repository

import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GenreDto


interface IGameRepository {
    suspend fun getGames(
        page: Int,
        genreId: String? = null,
        searchQuery: String? = null
    ): List<GameDto>

    suspend fun getGameDetails(id: Int): GameDto?

    suspend fun getGenres(): List<GenreDto>

    suspend fun clearCache()
}