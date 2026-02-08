package com.example.gamebrowser.data.model.response

import com.example.gamebrowser.data.model.dto.GenreDto
import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("results")
    val genres: List<GenreDto>
)
