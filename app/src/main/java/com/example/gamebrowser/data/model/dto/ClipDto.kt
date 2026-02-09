package com.example.gamebrowser.data.model.dto

import com.google.gson.annotations.SerializedName

data class ClipDto(
    val clip: String?,
    @SerializedName("clips")
    val clipsWrapper: ClipsWrapper?,
    val video: String?,
    val preview: String?
)