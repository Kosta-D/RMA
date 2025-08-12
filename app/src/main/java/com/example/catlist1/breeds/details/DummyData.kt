package com.example.catlist1.breeds.details

import com.example.catlist1.breeds.domain.Breed
import com.example.catlist1.breeds.domain.Weight


object DummyData {
    val breedList = listOf(
        Breed(
            id = "beng",
            name = "Bengal",
            alt_names = "Leopard Cat",
            description = "Bengals are confident and curious cats.",
            temperament = "Alert, Agile, Energetic",
            origin = "United States",
            life_span = "12 - 15",
            weight = Weight("3 - 6","4"),
            adaptability = 5,
            affection_level = 4,
            child_friendly = 4,
            dog_friendly = 5,
            energy_level = 5,
            grooming = 1,
            health_issues = 2,
            intelligence = 5,
            shedding_level = 3,
            social_needs = 4,
            stranger_friendly = 5,
            vocalisation = 3,
            rare = 0,
            wikipedia_url = "https://en.wikipedia.org/wiki/Bengal_(cat)"
        )
    )
}