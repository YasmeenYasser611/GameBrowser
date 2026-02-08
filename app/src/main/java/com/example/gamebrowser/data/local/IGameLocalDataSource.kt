package com.example.gamebrowser.data.local


import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GenreDto
import kotlinx.coroutines.flow.Flow

interface IGameLocalDataSource {

    // Games
    suspend fun getCachedGames(): List<GameDto>
    fun getCachedGamesFlow(): Flow<List<GameDto>>
    suspend fun getCachedGameById(gameId: Int): GameDto?
    suspend fun cacheGames(games: List<GameDto>)
    suspend fun cacheGame(game: GameDto)
    suspend fun clearGamesCache()
    suspend fun hasCachedGames(): Boolean

    // Genres
    suspend fun getCachedGenres(): List<GenreDto>
    fun getCachedGenresFlow(): Flow<List<GenreDto>>
    suspend fun cacheGenres(genres: List<GenreDto>)
    suspend fun clearGenresCache()
    suspend fun hasCachedGenres(): Boolean
}