package com.example.catlist1.breeds.gallery

import com.example.catlist1.breeds.data.api.model.BreedPhotoApiModel

interface BreedGalleryContract {

    data class UiState(
        val loading: Boolean = true,
        val photos: List<BreedPhotoUiModel> = emptyList()
    )
}