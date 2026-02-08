package com.example.gamebrowser.data.model.response

import com.example.gamebrowser.data.model.dto.GameDto
import com.google.gson.annotations.SerializedName

data class GamesResponse(
    val count: Int,

    @SerializedName("results")
    val games: List<GameDto>
)
