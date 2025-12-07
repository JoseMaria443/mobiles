package com.josemaria.examen3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritepokemon")
data class FavoritePokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String,
    val imageUrlShiny: String? = null,
    val height: Int = 0,
    val weight: Int = 0,
    val types: String = "",
    val abilities: String = "",
    val moves: String = ""
)