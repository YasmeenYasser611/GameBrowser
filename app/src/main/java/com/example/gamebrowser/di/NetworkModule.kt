package com.example.gamebrowser.di

import com.example.gamebrowser.data.remote.*
import com.example.gamebrowser.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGameService(): GameService =
        RetrofitHelper.retrofit.create(GameService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        service: GameService
    ): IGameRemoteDataSource =
        GameRemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: IGameRemoteDataSource
    ): IGameRepository =
        GameRepositoryImpl(remoteDataSource)
}
