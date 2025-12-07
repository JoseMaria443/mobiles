package com.josemaria.examen3.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josemaria.examen3.data.model.PokemonDetail
import com.josemaria.examen3.data.repository.PokemonRepository
import com.josemaria.examen3.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<PokemonDetail>>(emptyList())
    val searchResults: StateFlow<List<PokemonDetail>> = _searchResults
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching
    
    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> = _searchError

    init {
        startSearchFlow()
    }

    @OptIn(FlowPreview::class)
    private fun startSearchFlow() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .collect { query ->
                    if (query.isBlank()) {
                        _searchResults.value = emptyList()
                        _searchError.value = null
                    } else {
                        performSearch(query.trim().lowercase())
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private suspend fun performSearch(query: String) {
        _isSearching.value = true
        _searchError.value = null
        
        try {
            val allPokemonResult = repository.getPokemonList(1000, 0)
            if (allPokemonResult is Resource.Success) {
                val matchingPokemon = allPokemonResult.data?.filter { 
                    it.name.contains(query, ignoreCase = true)
                } ?: emptyList()
                
                val details = mutableListOf<PokemonDetail>()
                matchingPokemon.take(10).forEach { pokemon ->
                    val detailResult = repository.getPokemonDetail(pokemon.name)
                    if (detailResult.isSuccessful) {
                        detailResult.body()?.let { details.add(it) }
                    }
                }
                
                _searchResults.value = details
                if (details.isEmpty()) {
                    _searchError.value = "No se encontraron Pokémon que coincidan"
                }
            }
        } catch (e: Exception) {
            _searchError.value = "Error en la búsqueda: ${e.message}"
        } finally {
            _isSearching.value = false
        }
    }
}