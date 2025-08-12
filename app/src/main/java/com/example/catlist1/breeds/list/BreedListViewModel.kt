package com.example.catlist1.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.breeds.dd.data.remote.BreedRepositoryImpl
import com.example.catlist1.breeds.domain.Breed
import com.example.catlist1.breeds.domain.BreedRepository
import com.example.catlist1.breeds.domain.Weight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BreedListContract.UiState())
    val state = _state.asStateFlow()

    private fun setState(reducer: BreedListContract.UiState.() -> BreedListContract.UiState) =
        _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<BreedListContract.UiEvent>()

    init {
        //Log.d("BreedListVM", "init called")
        observeEvents()
        //loadBreeds()
        observeAllBreeds()
        syncBreeds()
    }

    fun setEvent(event: BreedListContract.UiEvent) = viewModelScope.launch {
        events.emit(event)
    }


    //novo
    private fun observeAllBreeds() = viewModelScope.launch {

        setState { copy(loading = true) }
        repository.observeAllBreeds().collect { breeds ->
            setState { copy(loading = false, data = breeds, error = null) }
        }



    }
    private fun syncBreeds() = viewModelScope.launch {
        try {
            repository.syncBreeds() // fetch sa api i insert u bazu
        } catch (e: Exception) {
            setState { copy(loading = false, error = e) }
        }
    }


    //

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is BreedListContract.UiEvent.Search -> search(event.query)
                    BreedListContract.UiEvent.LoadAll -> observeAllBreeds()
                }
            }
        }
    }

    //vise nije potreban
    private fun loadBreeds() = viewModelScope.launch {
        try {
            //Log.d("BreedListVM", "Loading breeds...")
            val data = repository.getAllBreeds().ifEmpty {
                Log.w("BreedListVM", "No data from API, using dummy fallback")
                listOf(
                    Breed(
                        id = "beng",
                        name = "Bengal",
                        alt_names = "Bengal Cat",
                        description = "Active, curious and playful.",
                        temperament = "Alert, Agile",
                        origin = "USA",
                        life_span = "12 - 15",
                        weight = Weight("4","7"),
                        adaptability = 5,
                        affection_level = 4,
                        child_friendly = 4,
                        dog_friendly = 5,
                        energy_level = 5,
                        grooming = 2,
                        health_issues = 3,
                        intelligence = 5,
                        shedding_level = 3,
                        social_needs = 5,
                        stranger_friendly = 4,
                        vocalisation = 3,
                        rare = 0,
                        wikipedia_url = "https://en.wikipedia.org/wiki/Bengal_cat"

                    ),
                    Breed(
                        id = "siam",
                        name = "Siamese",
                        alt_names = null,
                        description = "Very social and vocal.",
                        temperament = "Affectionate, Intelligent",
                        origin = "Thailand",
                        life_span = "10 - 13",
                        weight = Weight("5","3"),
                        adaptability = 4,
                        affection_level = 5,
                        child_friendly = 5,
                        dog_friendly = 4,
                        energy_level = 4,
                        grooming = 1,
                        health_issues = 2,
                        intelligence = 5,
                        shedding_level = 2,
                        social_needs = 5,
                        stranger_friendly = 5,
                        vocalisation = 5,
                        rare = 0,
                        wikipedia_url = "https://en.wikipedia.org/wiki/Siamese_cat"

                    )
                )
            }
            //Log.d("BreedListVM", "Loaded ${data.size} breeds")
            setState { copy(loading = false, data = data, error = null) }
        } catch (e: Exception) {
            //Log.e("BreedListVM", "Error loading breeds", e)
            setState { copy(loading = false, error = e) }
        }
    }

    private fun search(query: String) = viewModelScope.launch {
        try {
            //Log.d("BreedListVM", "Searching breeds for query: $query")
            val results = repository.searchBreeds(query)
            //Log.d("BreedListVM", "Found ${results.size} results")
            setState { copy(loading = false, data = results, error = null) }
        } catch (e: Exception) {
            //Log.e("BreedListVM", "Error during search", e)
            setState { copy(loading = false, error = e) }
        }
    }
}
