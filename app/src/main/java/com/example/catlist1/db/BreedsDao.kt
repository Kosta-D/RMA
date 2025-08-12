package com.example.catlist1.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedData>)

    @Query("SELECT * FROM BreedData")
    fun observeAllBreads(): Flow<List<BreedData>>

    @Query("SELECT * FROM BreedData WHERE id = :breedId")
    suspend fun getBreedById(breedId: String): BreedData?

    @Upsert
    fun upsertAllPhotos(data: List<BreedPhoto>)

    @Query("SELECT * FROM BreedPhoto WHERE breedId = :breedId")
    fun observeBreedPhotos(breedId: String): Flow<List<BreedPhoto>>

}