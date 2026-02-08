package com.example.gamebrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.gamebrowser.data.local.converter.GenreListConverter
import com.example.gamebrowser.data.local.converter.PlatformListConverter
import com.example.gamebrowser.data.model.dto.GameDto
import com.example.gamebrowser.data.model.dto.GenreDto
import com.example.gamebrowser.data.model.dto.PlatformDto
import com.example.gamebrowser.data.model.dto.PlatformWrapperDto



@Entity(tableName = "games")
@TypeConverters(GenreListConverter::class, PlatformListConverter::class)
data class GameEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val releaseDate: String?,
    val description: String?,
    val descriptionRaw: String?,
    val genres: List<GenreDto>?,
    val platforms: List<PlatformDto>?,
    val metacriticScore: Int?,
    val cachedAt: Long = System.currentTimeMillis()
)

// Extension functions for mapping between Entity and DTO
fun GameEntity.toDto(): GameDto {
    return GameDto(
        id = id,
        name = name,
        imageUrl = imageUrl,
        rating = rating,
        releaseDate = releaseDate,
        description = description,
        descriptionRaw = descriptionRaw,
        genres = genres,
        platforms = platforms?.map { PlatformWrapperDto(it) },
        metacriticScore = metacriticScore
    )
}

fun GameDto.toEntity(): GameEntity {
    return GameEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        rating = rating,
        releaseDate = releaseDate,
        description = description,
        descriptionRaw = descriptionRaw,
        genres = genres,
        platforms = platforms?.map { it.platform },
        metacriticScore = metacriticScore
    )
}