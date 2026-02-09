package com.example.gamebrowser.data.remote

import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.ShortScreenshotDto
import com.example.gamebrowser.data.model.response.GameMoviesResponse
import com.example.gamebrowser.data.model.response.GamesResponse
import com.example.gamebrowser.data.model.response.GenresResponse


interface IGameRemoteDataSource {
    suspend fun getGames(
        page: Int,
        genreId: String? = null,
        searchQuery: String? = null
    ): GamesResponse?

    suspend fun getGameDetails(id: Int): GameDto?

    suspend fun getGenres(): GenresResponse?

    suspend fun getGameScreenshots(id: Int): List<ShortScreenshotDto>

    suspend fun getGameMovies(id: Int): GameMoviesResponse?

}