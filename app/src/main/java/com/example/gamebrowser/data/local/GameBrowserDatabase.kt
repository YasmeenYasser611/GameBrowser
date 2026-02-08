package com.example.gamebrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gamebrowser.data.local.converter.GenreListConverter
import com.example.gamebrowser.data.local.converter.PlatformListConverter
import com.example.gamebrowser.data.local.dao.GameDao
import com.example.gamebrowser.data.local.dao.GenreDao
import com.example.gamebrowser.data.local.entity.GameEntity
import com.example.gamebrowser.data.local.entity.GenreEntity

@Database(
    entities = [GameEntity::class, GenreEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(GenreListConverter::class, PlatformListConverter::class)
abstract class GameBrowserDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun genreDao(): GenreDao
}