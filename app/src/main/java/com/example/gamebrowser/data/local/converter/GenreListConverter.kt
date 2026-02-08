package com.example.gamebrowser.data.local.converter

import androidx.room.TypeConverter
import com.example.gamebrowser.data.model.dto.GenreDto

import com.example.gamebrowser.data.model.dto.PlatformDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromGenreList(genres: List<GenreDto>?): String? {
        if (genres == null) return null
        return gson.toJson(genres)
    }

    @TypeConverter
    fun toGenreList(genresString: String?): List<GenreDto>? {
        if (genresString == null) return null
        val type = object : TypeToken<List<GenreDto>>() {}.type
        return gson.fromJson(genresString, type)
    }
}

class PlatformListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromPlatformList(platforms: List<PlatformDto>?): String? {
        if (platforms == null) return null
        return gson.toJson(platforms)
    }

    @TypeConverter
    fun toPlatformList(platformsString: String?): List<PlatformDto>? {
        if (platformsString == null) return null
        val type = object : TypeToken<List<PlatformDto>>() {}.type
        return gson.fromJson(platformsString, type)
    }
}