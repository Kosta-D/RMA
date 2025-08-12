package com.example.catlist1.breeds.domain

import com.example.catlist1.breeds.domain.Breed
import com.example.catlist1.db.BreedPhoto
import kotlinx.coroutines.flow.Flow

interface BreedRepository {
    suspend fun getAllBreeds(): List<Breed>
    suspend fun getBreedById(id: String): Breed?
    suspend fun searchBreeds(query: String): List<Breed>
    suspend fun syncBreeds()
    fun observeAllBreeds(): kotlinx.coroutines.flow.Flow<List<Breed>>
    suspend fun fetchBreedPhotos(breedId: String)
    fun observeBreedPhotos(breedId: String): Flow<List<BreedPhoto>>

}