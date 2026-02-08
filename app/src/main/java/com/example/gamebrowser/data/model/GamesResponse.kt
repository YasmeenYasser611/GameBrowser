package com.example.gamebrowser.data.model

import com.google.gson.annotations.SerializedName

data class GamesResponse(
    val count: Int,

    @SerializedName("results")
    val games: List<GameDto>
)
