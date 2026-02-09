package com.example.gamebrowser.data.repository

import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GameMovieDto
import com.example.gamebrowser.data.model.dto.GenreDto
import com.example.gamebrowser.data.model.dto.ShortScreenshotDto


interface IGameRepository {
    suspend fun getGames(
        page: Int,
        genreId: String? = null,
        searchQuery: String? = null
    ): List<GameDto>

    suspend fun getGameDetails(id: Int): GameDto?

    suspend fun getGenres(): List<GenreDto>

    suspend fun clearCache()

    suspend fun getGameScreenshots(id: Int): List<ShortScreenshotDto>

    suspend fun getGameMovies(id: Int): List<GameMovieDto>


}