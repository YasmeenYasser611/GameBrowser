package com.example.gamebrowser.data.remote

import android.util.Log

class GameRemoteDataSourceImpl(
    private val service: GameService
) : IGameRemoteDataSource {


    override suspend fun getGames(page: Int): GamesResponse? {
        return try {
            service.getGames(page = page)
        } catch (e: Exception) {
            Log.e("GameRemoteDataSource", "Network error", e)
            null
        }
    }


}
