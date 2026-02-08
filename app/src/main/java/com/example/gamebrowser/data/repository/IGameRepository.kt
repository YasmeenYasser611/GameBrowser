package com.example.gamebrowser.data.repository

import com.example.gamebrowser.data.model.GameDto

interface IGameRepository {
    suspend fun getGames(page: Int): List<GameDto>
}
