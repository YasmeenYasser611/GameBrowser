package com.example.gamebrowser.data.model.response

import com.example.gamebrowser.data.model.dto.GameMovieDto


data class GameMoviesResponse(
    val results: List<GameMovieDto>
)