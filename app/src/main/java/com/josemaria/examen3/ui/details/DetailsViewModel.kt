package com.josemaria.examen3.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josemaria.examen3.data.model.FavoritePokemon
import com.josemaria.examen3.data.model.PokemonDetail
import com.josemaria.examen3.data.model.toPokemonDetail
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
            
            // Intentar cargar desde API primero
            try {
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
            } catch (e: Exception) {
                // Si falla la API, intentar cargar desde caché local
                val cachedPokemon = repository.getFavoritePokemonByName(pokemonName)
                
                if (cachedPokemon != null) {
                    val detail = cachedPokemon.toPokemonDetail()
                    _pokemonDetail.value = Resource.Success(detail)
                    _isFavorite.value = true
                } else {
                    _pokemonDetail.value = Resource.Error("Sin conexión a internet. Solo puedes ver Pokémon favoritos sin conexión.")
                }
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
                imageUrl = detail.sprites.frontDefault ?: "",
                imageUrlShiny = detail.sprites.frontShiny,
                height = detail.height,
                weight = detail.weight,
                types = detail.types.joinToString(",") { it.type.name },
                abilities = detail.abilities.joinToString("|") { "${it.ability.name}:${it.isHidden}" },
                moves = detail.moves.take(10).joinToString(",") { it.move.name }
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