package com.example.gamebrowser.data.model.dto

import com.google.gson.annotations.SerializedName

data class GameDto(
    val id: Int,
    val name: String,
    @SerializedName("background_image")
    val imageUrl: String?,
    val rating: Double,
    @SerializedName("released")
    val releaseDate: String?,
    val description: String?,
    @SerializedName("description_raw")
    val descriptionRaw: String?,
    val genres: List<GenreDto>?,
    val platforms: List<PlatformWrapperDto>?,
    @SerializedName("metacritic")
    val metacriticScore: Int?
)






