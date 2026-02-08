package com.example.gamebrowser.data.local.dao

import androidx.room.*
import com.example.gamebrowser.data.local.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("SELECT * FROM genres ORDER BY name ASC")
    fun getAllGenres(): Flow<List<GenreEntity>>

    @Query("SELECT * FROM genres ORDER BY name ASC")
    suspend fun getAllGenresList(): List<GenreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("DELETE FROM genres")
    suspend fun deleteAllGenres()

    @Query("SELECT COUNT(*) FROM genres")
    suspend fun getGenresCount(): Int
}