package com.example.catlist1.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlist1.breeds.domain.Breed
import com.example.catlist1.breeds.domain.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {

    private val _breed = MutableStateFlow<Breed?>(null)
    val breed = _breed.asStateFlow()

    fun loadBreedById(id: String){
        viewModelScope.launch {
            try {
                _breed.value = repository.getBreedById(id)
            } catch (e: Exception) {
                // handle error
            }
        }
    }
}
