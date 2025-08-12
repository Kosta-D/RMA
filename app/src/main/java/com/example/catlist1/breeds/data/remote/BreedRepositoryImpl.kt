package com.example.catlist1.breeds.dd.data.remote

import com.example.catlist1.breeds.domain.Breed
import com.example.catlist1.breeds.data.api.TheCatApiService
import com.example.catlist1.breeds.domain.BreedRepository
import com.example.catlist1.breeds.mappers.asBreedData
import com.example.catlist1.breeds.mappers.asBreedDomain
import com.example.catlist1.breeds.mappers.asBreedPhotoDbModel
import com.example.catlist1.db.BreedPhoto
import com.example.catlist1.db.BreedsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val api: TheCatApiService,
    private val dao: BreedsDao
) : BreedRepository {


    override suspend fun getAllBreeds(): List<Breed> {
        return dao.observeAllBreads().map { list -> list.map { it.asBreedDomain() } }.first()
    }

    override suspend fun getBreedById(id: String): Breed? {
        return dao.getBreedById(id)?.asBreedDomain()
    }

    override suspend fun searchBreeds(query: String): List<Breed> {
        val results = api.searchBreeds(query)
        return results
    }

    override suspend fun syncBreeds() = withContext(Dispatchers.IO) {
        val breeds = api.getAllBreeds()
       dao.insertAll(breeds.map { it.asBreedData() })
    }

    override fun observeAllBreeds(): Flow<List<Breed>> {
        return dao.observeAllBreads().map { list->list.map{it.asBreedDomain()} }
    }


    override suspend fun fetchBreedPhotos(breedId: String) = withContext(Dispatchers.IO) {
        try {
            val photos = api.getBreedPhotos(breedId = breedId)
            dao.upsertAllPhotos(photos.map { it.asBreedPhotoDbModel(breedId = breedId) })
        } catch (e: Exception) {
            println("Failed to fetch photos for breedId=$breedId: ${e.message}")
        }
    }

    override fun observeBreedPhotos(breedId: String): Flow<List<BreedPhoto>> {
        return dao.observeBreedPhotos(breedId)
    }


    /*staro iz prvog dela
    private var cache: List<Breed>? = null

    override suspend fun getAllBreeds(): List<Breed> {
        return cache ?: api.getAllBreeds().also { cache = it }
    }

    override suspend fun getBreedById(id: String): Breed? {
        return getAllBreeds().find { it.id == id }
    }

    override suspend fun searchBreeds(query: String): List<Breed> {
        return api.searchBreeds(query)
    }

     */
}