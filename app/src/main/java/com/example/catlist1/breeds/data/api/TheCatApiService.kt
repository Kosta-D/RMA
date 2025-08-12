package com.example.catlist1.breeds.data.api


import com.example.catlist1.breeds.data.api.model.BreedPhotoApiModel
import com.example.catlist1.breeds.domain.Breed
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApiService {

    @GET("breeds")
    suspend fun getAllBreeds(): List<Breed>

    @GET("breeds/search")
    suspend fun searchBreeds(@Query("q") query: String): List<Breed>

    @GET("images/search")
    suspend fun getBreedPhotos(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 10
    ): List<BreedPhotoApiModel>

}