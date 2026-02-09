package com.example.gamebrowser.data.model.dto

import com.google.gson.annotations.SerializedName

data class ClipsWrapper(
    @SerializedName("320")
    val quality320: String?,
    @SerializedName("640")
    val quality640: String?,
    val full: String?
)