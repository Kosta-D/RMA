package com.example.catlist1.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BreedData::class,
        BreedPhoto::class
    ],
    version = 2
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun breedsDao(): BreedsDao
}