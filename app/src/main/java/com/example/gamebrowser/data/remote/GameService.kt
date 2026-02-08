package com.example.gamebrowser.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GameService {

    // Popular games list
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): GamesResponse


}
