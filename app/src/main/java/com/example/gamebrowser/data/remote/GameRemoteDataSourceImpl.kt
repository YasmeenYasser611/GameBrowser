package com.example.gamebrowser.data.remote

import android.util.Log
import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.ShortScreenshotDto
import com.example.gamebrowser.data.model.response.GameMoviesResponse
import com.example.gamebrowser.data.model.response.GamesResponse
import com.example.gamebrowser.data.model.response.GenresResponse

import javax.inject.Inject

class GameRemoteDataSourceImpl @Inject constructor(
    private val service: GameService
) : IGameRemoteDataSource {

    override suspend fun getGames(
        page: Int,
        genreId: String?,
        searchQuery: String?
    ): GamesResponse? {
        return try {
            service.getGames(
                page = page,
                genres = genreId,
                search = searchQuery
            )
        } catch (e: Exception) {
            Log.e("GameRemoteDataSource", "Network error", e)
            null
        }
    }

    override suspend fun getGameDetails(id: Int): GameDto? {
        return try {
            service.getGameDetails(id)
        } catch (e: Exception) {
            Log.e("GameRemoteDataSource", "Error fetching game details", e)
            null
        }
    }

    override suspend fun getGenres(): GenresResponse? {
        return try {
            service.getGenres()
        } catch (e: Exception) {
            Log.e("GameRemoteDataSource", "Error fetching genres", e)
            null
        }
    }

    override suspend fun getGameScreenshots(id: Int): List<ShortScreenshotDto> {
        return try {
            service.getGameScreenshots(id).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getGameMovies(id: Int): GameMoviesResponse? {
        return service.getGameMovies(id)
    }

}