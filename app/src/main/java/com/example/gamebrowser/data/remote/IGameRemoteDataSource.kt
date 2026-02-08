package com.example.gamebrowser.data.remote

interface IGameRemoteDataSource {
    suspend fun getGames(page: Int): GamesResponse?
}
