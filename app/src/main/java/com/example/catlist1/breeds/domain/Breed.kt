package com.example.catlist1.breeds.domain

import kotlinx.serialization.Serializable

@Serializable
data class Breed(
    val id: String,
    val name: String,
    val alt_names: String? = null,
    val description: String,
    val temperament: String,
    val origin: String,
    val life_span: String,
    val weight: Weight,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val grooming: Int,
    val health_issues: Int,
    val intelligence: Int,
    val shedding_level: Int,
    val social_needs: Int,
    val stranger_friendly: Int,
    val vocalisation: Int,
    val rare: Int,
    val wikipedia_url: String? = null,
    val reference_image_id: String? = null//fix za sliku
)