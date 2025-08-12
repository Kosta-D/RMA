package com.example.catlist1.breeds.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.breeds.dd.data.remote.BreedRepositoryImpl
import com.example.catlist1.breeds.domain.BreedRepository
import com.example.catlist1.db.BreedPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BreedGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepositoryImpl
):ViewModel(){

    private val breedId: String = checkNotNull(savedStateHandle["breedId"])

    private val _state = MutableStateFlow(BreedGalleryContract.UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedGalleryContract.UiState.() -> BreedGalleryContract.UiState) = _state.update(reducer)

    init {
        fetchBreedPhotos()
        observeBreedPhotos()
    }

    private fun fetchBreedPhotos() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreedPhotos(breedId)
                }
            } catch (e: Exception) {
            }
            setState { copy(loading = false) }
        }
    }

    private fun observeBreedPhotos() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.observeBreedPhotos(breedId)
                    .distinctUntilChanged()
                    .collect {
                        setState {
                            copy(photos = it.map { photo -> photo.asBreedPhotoUiModel() })
                        }
                    }
            }
        }
    }


    private fun BreedPhoto.asBreedPhotoUiModel() = BreedPhotoUiModel(
        photoId = this.photoId,
        imageUrl = this.url
    )

}