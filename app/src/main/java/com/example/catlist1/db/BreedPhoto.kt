package com.example.catlist1.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BreedPhoto(
    @PrimaryKey val photoId: String,
    val breedId: String,
    val url: String
)
