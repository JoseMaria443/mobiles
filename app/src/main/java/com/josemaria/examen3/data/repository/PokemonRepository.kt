package com.josemaria.examen3.data.repository

import com.josemaria.examen3.data.local.PokemonDao
import com.josemaria.examen3.data.model.FavoritePokemon
import com.josemaria.examen3.data.model.Pokemon
import com.josemaria.examen3.data.remote.PokemonApiService
import com.josemaria.examen3.util.Resource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class PokemonRepository @Inject constructor(
    private val api: PokemonApiService,
    private val dao: PokemonDao
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<List<Pokemon>> {
        return try {
            val response = api.getPokemonList(limit, offset)
            if (response.isSuccessful) {
                Resource.Success(response.body()?.results ?: emptyList())
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de red desconocido")
        }
    }

    suspend fun insertFavoritePokemon(pokemon: FavoritePokemon) {
        dao.insertFavorite(pokemon)
    }

    suspend fun deleteFavoritePokemon(pokemon: FavoritePokemon) {
        dao.deleteFavorite(pokemon)
    }

    fun getAllFavorites(): Flow<List<FavoritePokemon>> {
        return dao.getAllFavorites()
    }

    suspend fun isPokemonFavorite(pokemonId: Int): Boolean {
        return dao.isPokemonFavorite(pokemonId)
    }
    
    suspend fun getFavoritePokemonByName(name: String): FavoritePokemon? {
        return dao.getFavoritePokemonByName(name)
    }

    suspend fun getPokemonDetail(name: String) = api.getPokemonDetail(name)
}