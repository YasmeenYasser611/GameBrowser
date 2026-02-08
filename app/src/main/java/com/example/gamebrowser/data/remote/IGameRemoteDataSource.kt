package com.example.gamebrowser.data.remote

import com.example.gamebrowser.data.model.GamesResponse

interface IGameRemoteDataSource {
    suspend fun getGames(page: Int): GamesResponse?
}
