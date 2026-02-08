package com.example.gamebrowser.data.repository


import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GenreDto
import com.example.gamebrowser.data.remote.IGameRemoteDataSource
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val remoteDataSource: IGameRemoteDataSource
) : IGameRepository {

    override suspend fun getGames(
        page: Int,
        genreId: String?,
        searchQuery: String?
    ): List<GameDto> {
        return remoteDataSource
            .getGames(page, genreId, searchQuery)
            ?.games
            ?: emptyList()
    }

    override suspend fun getGameDetails(id: Int): GameDto? {
        return remoteDataSource.getGameDetails(id)
    }

    override suspend fun getGenres(): List<GenreDto> {
        return remoteDataSource
            .getGenres()
            ?.genres
            ?: emptyList()
    }
}