package com.example.catlist1.leaderboard.di

import com.example.catlist1.leaderboard.network.LeaderboardApi
import com.example.catlist1.leaderboard.repository.LeaderboardRepository
import com.example.catlist1.leaderboard.repository.LeaderboardRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Provides
    @Singleton
    fun provideLeaderboardApi(): LeaderboardApi {
        return Retrofit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LeaderboardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLeaderboardRepository(api: LeaderboardApi): LeaderboardRepository {
        return LeaderboardRepositoryImpl(api)
    }

}