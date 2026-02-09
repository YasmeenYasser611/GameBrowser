package com.example.gamebrowser.data.repository


import com.example.gamebrowser.data.local.IGameLocalDataSource
import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GameMovieDto
import com.example.gamebrowser.data.model.dto.GenreDto
import com.example.gamebrowser.data.model.dto.ShortScreenshotDto

import com.example.gamebrowser.data.remote.IGameRemoteDataSource
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val remoteDataSource: IGameRemoteDataSource,
    private val localDataSource: IGameLocalDataSource
) : IGameRepository {


    override suspend fun getGames(
        page: Int,
        genreId: String?,
        searchQuery: String?
    ): List<GameDto> {
        return try {

            val networkGames = remoteDataSource
                .getGames(page, genreId, searchQuery)
                ?.games

            if (!networkGames.isNullOrEmpty()) {

                if (page == 1 && genreId == null && searchQuery == null) {
                    localDataSource.cacheGames(networkGames)
                }
                networkGames
            } else if (page == 1 && genreId == null && searchQuery == null) {

                localDataSource.getCachedGames()
            } else {
                emptyList()
            }
        } catch (e: Exception) {

            if (page == 1 && genreId == null && searchQuery == null) {
                localDataSource.getCachedGames()
            } else {
                emptyList()
            }
        }
    }


    override suspend fun getGameDetails(id: Int): GameDto? {
        return remoteDataSource.getGameDetails(id)
    }




    override suspend fun getGenres(): List<GenreDto> {
        return try {

            if (localDataSource.hasCachedGenres()) {
                localDataSource.getCachedGenres()
            } else {

                val networkGenres = remoteDataSource
                    .getGenres()
                    ?.genres
                    ?: emptyList()

                if (networkGenres.isNotEmpty()) {

                    localDataSource.cacheGenres(networkGenres)
                }
                networkGenres
            }
        } catch (e: Exception) {

            localDataSource.getCachedGenres()
        }
    }

    override suspend fun clearCache() {
        localDataSource.clearGamesCache()
        localDataSource.clearGenresCache()
    }

    override suspend fun getGameScreenshots(id: Int): List<ShortScreenshotDto> {
        return remoteDataSource.getGameScreenshots(id)
    }

    override suspend fun getGameMovies(id: Int): List<GameMovieDto> {
        return try {
            remoteDataSource.getGameMovies(id)?.results ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }



}