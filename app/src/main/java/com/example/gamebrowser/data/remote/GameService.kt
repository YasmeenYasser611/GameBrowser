package com.example.gamebrowser.data.remote

import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.response.GameMoviesResponse
import com.example.gamebrowser.data.model.response.GamesResponse
import com.example.gamebrowser.data.model.response.GenresResponse
import com.example.gamebrowser.data.model.response.ScreenshotsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {

    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("genres") genres: String? = null,
        @Query("search") search: String? = null
    ): GamesResponse

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Int
    ): GameDto

    @GET("genres")
    suspend fun getGenres(): GenresResponse

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(
        @Path("id") id: Int
    ): ScreenshotsResponse

    @GET("games/{id}/movies")
    suspend fun getGameMovies(
        @Path("id") id: Int,

    ): GameMoviesResponse


}