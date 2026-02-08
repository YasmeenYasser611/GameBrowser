package com.example.gamebrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gamebrowser.data.model.dto.GenreDto


@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val cachedAt: Long = System.currentTimeMillis()
)

// Extension functions for mapping
fun GenreEntity.toDto(): GenreDto {
    return GenreDto(
        id = id,
        name = name
    )
}

fun GenreDto.toEntity(): GenreEntity {
    return GenreEntity(
        id = id,
        name = name
    )
}