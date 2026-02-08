package com.example.gamebrowser.data.local.dao

import androidx.room.*
import com.example.gamebrowser.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM games ORDER BY cachedAt DESC")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games ORDER BY cachedAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getGamesPaged(limit: Int, offset: Int): List<GameEntity>

    @Query("SELECT * FROM games WHERE id = :gameId")
    suspend fun getGameById(gameId: Int): GameEntity?

    @Query("SELECT * FROM games WHERE id = :gameId")
    fun getGameByIdFlow(gameId: Int): Flow<GameEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("DELETE FROM games")
    suspend fun deleteAllGames()

    @Query("DELETE FROM games WHERE cachedAt < :timestamp")
    suspend fun deleteOldGames(timestamp: Long)

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getGamesCount(): Int
}