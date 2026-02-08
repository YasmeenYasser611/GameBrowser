package com.example.gamebrowser.di

import android.content.Context
import androidx.room.Room
import com.example.gamebrowser.data.local.GameBrowserDatabase
import com.example.gamebrowser.data.local.GameLocalDataSourceImpl
import com.example.gamebrowser.data.local.IGameLocalDataSource
import com.example.gamebrowser.data.local.dao.GameDao
import com.example.gamebrowser.data.local.dao.GenreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGameBrowserDatabase(
        @ApplicationContext context: Context
    ): GameBrowserDatabase {
        return Room.databaseBuilder(
            context,
            GameBrowserDatabase::class.java,
            "game_browser_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGameDao(database: GameBrowserDatabase): GameDao {
        return database.gameDao()
    }

    @Provides
    @Singleton
    fun provideGenreDao(database: GameBrowserDatabase): GenreDao {
        return database.genreDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: GameBrowserDatabase
    ): IGameLocalDataSource {
        return GameLocalDataSourceImpl(database)
    }
}