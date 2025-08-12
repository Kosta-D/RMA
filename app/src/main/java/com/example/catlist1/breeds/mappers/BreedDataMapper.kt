package com.example.catlist1.breeds.mappers

import com.example.catlist1.breeds.data.api.model.BreedPhotoApiModel
import com.example.catlist1.breeds.domain.Breed
import com.example.catlist1.db.BreedData
import com.example.catlist1.db.BreedPhoto

// Domain → DB
fun Breed.asBreedData(): BreedData {
    return BreedData(
        id = id,
        name = name,
        alt_names = alt_names,
        description = description,
        temperament = temperament,
        origin = origin,
        life_span = life_span,
        weight = weight,
        adaptability = adaptability,
        affection_level = affection_level,
        child_friendly = child_friendly,
        dog_friendly = dog_friendly,
        energy_level = energy_level,
        grooming = grooming,
        health_issues = health_issues,
        intelligence = intelligence,
        shedding_level = shedding_level,
        social_needs = social_needs,
        stranger_friendly = stranger_friendly,
        vocalisation = vocalisation,
        rare = rare,
        wikipedia_url = wikipedia_url,
        reference_image_id = reference_image_id
    )
}

// DB → Domain
fun BreedData.asBreedDomain(): Breed {
    return Breed(
        id = id,
        name = name,
        alt_names = alt_names,
        description = description,
        temperament = temperament,
        origin = origin,
        life_span = life_span,
        weight = weight,
        adaptability = adaptability,
        affection_level = affection_level,
        child_friendly = child_friendly,
        dog_friendly = dog_friendly,
        energy_level = energy_level,
        grooming = grooming,
        health_issues = health_issues,
        intelligence = intelligence,
        shedding_level = shedding_level,
        social_needs = social_needs,
        stranger_friendly = stranger_friendly,
        vocalisation = vocalisation,
        rare = rare,
        wikipedia_url = wikipedia_url,
        reference_image_id = reference_image_id
    )
}

fun BreedPhotoApiModel.asBreedPhotoDbModel(breedId: String): BreedPhoto {
    return BreedPhoto(
        photoId = this.photoId,
        breedId = breedId,
        url = this.url
    )
}