package com.example.catlist1.quiz.repository

import com.example.catlist1.breeds.domain.BreedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizModule {

    @Provides
    @Singleton
    fun provideQuizRepository(
        breedRepository: BreedRepository
    ): QuizRepository=QuizRepositoryImpl(breedRepository)
}