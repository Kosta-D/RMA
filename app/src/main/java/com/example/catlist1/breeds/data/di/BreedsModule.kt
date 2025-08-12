package com.example.catlist1.breeds.dd.data.di


import android.content.Context
import androidx.compose.ui.graphics.vector.RootGroupName
import androidx.room.Room
import com.example.catlist1.breeds.data.api.TheCatApiService
import com.example.catlist1.breeds.dd.data.remote.BreedRepositoryImpl
import com.example.catlist1.breeds.domain.BreedRepository
import com.example.catlist1.db.AppDatabase
import com.example.catlist1.db.BreedsDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedsModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(json: Json, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideCatApiService(retrofit: Retrofit): TheCatApiService {
        return retrofit.create(TheCatApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBreedRepository(api: TheCatApiService,breedsDao: BreedsDao): BreedRepository {
        return BreedRepositoryImpl(api,breedsDao)
    }

    //2. deo
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "catlist-db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesBreedsDao(appDatabase: AppDatabase):BreedsDao{
        return appDatabase.breedsDao()
    }

}