package com.josemaria.examen3.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josemaria.examen3.data.model.FavoritePokemon
import com.josemaria.examen3.data.model.PokemonDetail
import com.josemaria.examen3.data.repository.PokemonRepository
import com.josemaria.examen3.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow<Resource<PokemonDetail>>(Resource.Loading())
    val pokemonDetail: StateFlow<Resource<PokemonDetail>> = _pokemonDetail

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadPokemonDetail(pokemonName: String) {
        viewModelScope.launch {
            _pokemonDetail.value = Resource.Loading()
            val response = repository.getPokemonDetail(pokemonName)

            if (response.isSuccessful) {
                val detail = response.body()
                if (detail != null) {
                    _pokemonDetail.value = Resource.Success(detail)
                    checkIfFavorite(detail.id)
                } else {
                    _pokemonDetail.value = Resource.Error("Detalles no encontrados.")
                }
            } else {
                _pokemonDetail.value = Resource.Error(response.message())
            }
        }
    }

    private fun checkIfFavorite(pokemonId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isPokemonFavorite(pokemonId)
        }
    }

    fun toggleFavorite(detail: PokemonDetail) {
        viewModelScope.launch {
            val isCurrentlyFavorite = _isFavorite.value
            val favoritePokemon = FavoritePokemon(
                id = detail.id,
                name = detail.name,
                imageUrl = detail.sprites.frontDefault ?: ""
            )

            if (isCurrentlyFavorite) {
                repository.deleteFavoritePokemon(favoritePokemon)
            } else {
                repository.insertFavoritePokemon(favoritePokemon)
            }
            _isFavorite.value = !isCurrentlyFavorite
        }
    }
}