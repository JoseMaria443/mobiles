package com.josemaria.examen3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.josemaria.examen3.data.model.FavoritePokemon

@Database(
    entities = [FavoritePokemon::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    companion object {
        const val DATABASE_NAME = "pokemon_db"
    }
}