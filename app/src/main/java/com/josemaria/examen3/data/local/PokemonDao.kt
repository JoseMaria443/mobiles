package com.josemaria.examen3.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.josemaria.examen3.data.model.FavoritePokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(pokemon: FavoritePokemon)

    @Delete
    suspend fun deleteFavorite(pokemon: FavoritePokemon)

    @Query("SELECT * FROM favoritepokemon")
    fun getAllFavorites(): Flow<List<FavoritePokemon>>

    @Query("SELECT EXISTS(SELECT * FROM favoritepokemon WHERE id = :pokemonId)")
    suspend fun isPokemonFavorite(pokemonId: Int): Boolean
}