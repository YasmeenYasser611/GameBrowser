package com.example.gamebrowser.data.model.dto

data class GameMovieDto(
    val id: Int,
    val data: MovieDataDto
)

data class MovieDataDto(
    val max: String?,
    val `480`: String?
)
