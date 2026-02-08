package com.example.gamebrowser.data.model

import com.google.gson.annotations.SerializedName

data class GameDto(
    val id: Int,
    val name: String,

    @SerializedName("background_image")
    val imageUrl: String?,

    val rating: Double,

    @SerializedName("released")
    val releaseDate: String?
)
