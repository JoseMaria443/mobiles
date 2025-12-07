package com.josemaria.examen3.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josemaria.examen3.data.model.FavoritePokemon
import com.josemaria.examen3.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    val favoritePokemonList: StateFlow<List<FavoritePokemon>> =
        repository.getAllFavorites()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun deleteFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            repository.deleteFavoritePokemon(pokemon)
        }
    }
}