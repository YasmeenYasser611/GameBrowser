package com.example.gamebrowser.data.repository

import com.example.gamebrowser.data.model.GameDto
import com.example.gamebrowser.data.remote.IGameRemoteDataSource
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val remoteDataSource: IGameRemoteDataSource
) : IGameRepository {

    override suspend fun getGames(page: Int): List<GameDto> {
        return remoteDataSource
            .getGames(page)
            ?.games
            ?: emptyList()
    }
}
