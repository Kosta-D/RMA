package com.example.catlist1.breeds.list

import com.example.catlist1.breeds.domain.Breed


interface BreedListContract {

    data class UiState(
        val loading: Boolean = true,
        val data: List<Breed> = emptyList(),
        val error: Throwable? = null
    )

    sealed class UiEvent {
        data class Search(val query: String) : UiEvent()
        data object LoadAll : UiEvent()
    }
}