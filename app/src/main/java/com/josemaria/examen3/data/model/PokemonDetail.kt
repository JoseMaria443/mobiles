package com.josemaria.examen3.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val abilities: List<AbilitySlot>,
    val moves: List<MoveSlot>
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?
)

data class TypeSlot(
    val type: Type
)

data class Type(
    val name: String
)

data class AbilitySlot(
    val ability: Ability,
    @SerializedName("is_hidden")
    val isHidden: Boolean
)

data class Ability(
    val name: String
)

data class MoveSlot(
    val move: Move
)

data class Move(
    val name: String
)

// Función de extensión para convertir FavoritePokemon a PokemonDetail
fun FavoritePokemon.toPokemonDetail(): PokemonDetail {
    val typesList = types?.split(",")?.map { TypeSlot(Type(it.trim())) } ?: emptyList()
    
    val abilitiesList = abilities?.split("|")?.mapNotNull { abilityStr ->
        val parts = abilityStr.split(":")
        if (parts.size == 2) {
            AbilitySlot(
                ability = Ability(parts[0].trim()),
                isHidden = parts[1].trim().toBoolean()
            )
        } else null
    } ?: emptyList()
    
    val movesList = moves?.split(",")?.map { MoveSlot(Move(it.trim())) } ?: emptyList()
    
    return PokemonDetail(
        id = id,
        name = name,
        height = height ?: 0,
        weight = weight ?: 0,
        sprites = Sprites(
            frontDefault = imageUrl,
            frontShiny = imageUrlShiny
        ),
        types = typesList,
        abilities = abilitiesList,
        moves = movesList
    )
}