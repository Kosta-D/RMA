package com.example.catlist1.breeds.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BreedPhotoApiModel(
    @SerialName("id") val photoId: String,
    @SerialName("url") val url: String
)
