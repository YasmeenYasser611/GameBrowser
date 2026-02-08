package com.example.gamebrowser.data.local

import com.example.gamebrowser.data.local.entity.toDto
import com.example.gamebrowser.data.local.entity.toEntity
import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GenreDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameLocalDataSourceImpl @Inject constructor(
    private val database: GameBrowserDatabase
) : IGameLocalDataSource {

    private val gameDao = database.gameDao()
    private val genreDao = database.genreDao()

    // Games
    override suspend fun getCachedGames(): List<GameDto> {
        return gameDao.getGamesPaged(limit = 100, offset = 0).map { it.toDto() }
    }

    override fun getCachedGamesFlow(): Flow<List<GameDto>> {
        return gameDao.getAllGames().map { entities ->
            entities.map { it.toDto() }
        }
    }

    override suspend fun getCachedGameById(gameId: Int): GameDto? {
        return gameDao.getGameById(gameId)?.toDto()
    }

    override suspend fun cacheGames(games: List<GameDto>) {
        gameDao.insertGames(games.map { it.toEntity() })
    }

    override suspend fun cacheGame(game: GameDto) {
        gameDao.insertGame(game.toEntity())
    }

    override suspend fun clearGamesCache() {
        gameDao.deleteAllGames()
    }

    override suspend fun hasCachedGames(): Boolean {
        return gameDao.getGamesCount() > 0
    }

    // Genres
    override suspend fun getCachedGenres(): List<GenreDto> {
        return genreDao.getAllGenresList().map { it.toDto() }
    }

    override fun getCachedGenresFlow(): Flow<List<GenreDto>> {
        return genreDao.getAllGenres().map { entities ->
            entities.map { it.toDto() }
        }
    }

    override suspend fun cacheGenres(genres: List<GenreDto>) {
        genreDao.insertGenres(genres.map { it.toEntity() })
    }

    override suspend fun clearGenresCache() {
        genreDao.deleteAllGenres()
    }

    override suspend fun hasCachedGenres(): Boolean {
        return genreDao.getGenresCount() > 0
    }
}