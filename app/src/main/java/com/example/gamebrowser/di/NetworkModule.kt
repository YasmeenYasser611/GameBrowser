package com.example.gamebrowser.di

import com.example.gamebrowser.data.remote.GameService
import com.example.gamebrowser.data.remote.GameRemoteDataSourceImpl
import com.example.gamebrowser.data.remote.IGameRemoteDataSource
import com.example.gamebrowser.data.remote.RetrofitHelper
import com.example.gamebrowser.data.repository.GameRepositoryImpl
import com.example.gamebrowser.data.repository.IGameRepository
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
    fun provideGameService(): GameService {
        return RetrofitHelper.retrofit.create(GameService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        service: GameService
    ): IGameRemoteDataSource {
        return GameRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        remoteDataSource: IGameRemoteDataSource
    ): IGameRepository {
        return GameRepositoryImpl(remoteDataSource)
    }
}
