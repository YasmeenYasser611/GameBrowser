package com.example.gamebrowser.data.repository

import com.example.gamebrowser.data.local.IGameLocalDataSource
import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GenreDto

import com.example.gamebrowser.data.remote.IGameRemoteDataSource
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val remoteDataSource: IGameRemoteDataSource,
    private val localDataSource: IGameLocalDataSource
) : IGameRepository {

    /**
     * Cache-first strategy for getting games
     * 1. Try to fetch from network
     * 2. If successful, cache the results
     * 3. If network fails, return cached data
     * 4. If both fail, return empty list
     */
    override suspend fun getGames(
        page: Int,
        genreId: String?,
        searchQuery: String?
    ): List<GameDto> {
        return try {
            // Try network first
            val networkGames = remoteDataSource
                .getGames(page, genreId, searchQuery)
                ?.games

            if (!networkGames.isNullOrEmpty()) {
                // Cache the results if page 1 (fresh data)
                if (page == 1 && genreId == null && searchQuery == null) {
                    localDataSource.cacheGames(networkGames)
                }
                networkGames
            } else if (page == 1 && genreId == null && searchQuery == null) {
                // If network returned empty and it's first page, try cache
                localDataSource.getCachedGames()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // Network failed, try cache
            if (page == 1 && genreId == null && searchQuery == null) {
                localDataSource.getCachedGames()
            } else {
                emptyList()
            }
        }
    }

    /**
     * Cache-first strategy for game details
     * 1. Check cache first
     * 2. If not found or outdated, fetch from network
     * 3. Cache the fresh data
     */
    override suspend fun getGameDetails(id: Int): GameDto? {
        return try {
            // Try network first for fresh data
            val networkGame = remoteDataSource.getGameDetails(id)

            if (networkGame != null) {
                // Cache the fresh data
                localDataSource.cacheGame(networkGame)
                networkGame
            } else {
                // Network returned null, try cache
                localDataSource.getCachedGameById(id)
            }
        } catch (e: Exception) {
            // Network failed, return cached version
            localDataSource.getCachedGameById(id)
        }
    }

    /**
     * Cache-first strategy for genres
     * 1. Check if we have cached genres
     * 2. If yes, return cached (genres rarely change)
     * 3. If no, fetch from network and cache
     */
    override suspend fun getGenres(): List<GenreDto> {
        return try {
            // Check cache first
            if (localDataSource.hasCachedGenres()) {
                localDataSource.getCachedGenres()
            } else {
                // No cache, fetch from network
                val networkGenres = remoteDataSource
                    .getGenres()
                    ?.genres
                    ?: emptyList()

                if (networkGenres.isNotEmpty()) {
                    // Cache the genres
                    localDataSource.cacheGenres(networkGenres)
                }
                networkGenres
            }
        } catch (e: Exception) {
            // Network failed, try cache anyway
            localDataSource.getCachedGenres()
        }
    }

    override suspend fun clearCache() {
        localDataSource.clearGamesCache()
        localDataSource.clearGenresCache()
    }
}