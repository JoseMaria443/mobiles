package com.josemaria.examen3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritepokemon")
data class FavoritePokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String
)