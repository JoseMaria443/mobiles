package com.josemaria.examen3.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josemaria.examen3.data.model.Pokemon
import com.josemaria.examen3.data.repository.PokemonRepository
import com.josemaria.examen3.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<Resource<List<Pokemon>>>(Resource.Loading())
    val pokemonList: StateFlow<Resource<List<Pokemon>>> = _pokemonList

    private var currentPage = 0
    private val limit = 20
    private var canPaginate = true

    private val _favoritesIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoritesIds: StateFlow<Set<Int>> = _favoritesIds

    init {
        loadPokemonPaginated()
        loadFavorites()
    }

    fun loadPokemonPaginated() {
        if (!canPaginate || _pokemonList.value is Resource.Loading && _pokemonList.value.data != null) return

        if (_pokemonList.value.data == null) {
            _pokemonList.value = Resource.Loading()
        }

        val offset = currentPage * limit

        viewModelScope.launch {
            val result = repository.getPokemonList(limit, offset)

            _pokemonList.value = when (result) {
                is Resource.Success -> {
                    if (result.data.isNullOrEmpty()) {
                        canPaginate = false
                        Resource.Success(_pokemonList.value.data ?: emptyList())
                    } else {
                        val currentList = (_pokemonList.value.data ?: emptyList()) + result.data
                        currentPage++
                        Resource.Success(currentList)
                    }
                }
                is Resource.Error -> {
                    canPaginate = false
                    Resource.Error(result.message ?: "Error desconocido", _pokemonList.value.data)
                }
                else -> Resource.Error("Estado inválido", _pokemonList.value.data)
            }
        }
    }

    fun isListEndReached(): Boolean {
        return !canPaginate
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getAllFavorites().collect { favorites ->
                _favoritesIds.value = favorites.map { it.id }.toSet()
            }
        }
    }

    fun toggleFavorite(pokemonId: Int, pokemonName: String, imageUrl: String) {
        viewModelScope.launch {
            val isFavorite = _favoritesIds.value.contains(pokemonId)
            val favoritePokemon = com.josemaria.examen3.data.model.FavoritePokemon(
                id = pokemonId,
                name = pokemonName,
                imageUrl = imageUrl
            )
            
            if (isFavorite) {
                repository.deleteFavoritePokemon(favoritePokemon)
            } else {
                repository.insertFavoritePokemon(favoritePokemon)
            }
        }
    }
}