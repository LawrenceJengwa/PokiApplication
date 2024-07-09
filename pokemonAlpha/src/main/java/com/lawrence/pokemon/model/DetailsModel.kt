package com.lawrence.pokemon.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsModel(
    @SerializedName("name") var name: String,
    @SerializedName("weight") var weight: Int,
    @SerializedName("height") var height: Int,
    @SerializedName("species") var species: Specie,
    @SerializedName("types") var types: List<PokemonDetailsTypeItemModel>,
    @SerializedName("sprites") var sprite: PokemonDetailsSpritesModel,
    @SerializedName("abilities") var abilities: List<PokemonAbilityModel>,
    @SerializedName("stats") var stat: List<PokemonStat>,
    @SerializedName("moves") var moves: List<Moves>,
) : Parcelable {
    constructor() : this(
        name = "",
        weight = 0,
        height = 0,
        species = Specie(),
        types = listOf(),
        sprite = PokemonDetailsSpritesModel(),
        abilities = listOf(),
        stat = listOf(),
        moves = listOf(),
    )
}

@Parcelize
data class PokemonDetailsTypeModel(
    @SerializedName("name") val name: String,
) : Parcelable

@Parcelize
data class PokemonDetailsTypeItemModel(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: PokemonDetailsTypeModel,
) : Parcelable

@Parcelize
data class PokemonDetailsSpritesModel(
    @SerializedName("front_default") val imageURL: String = "",
) : Parcelable

@Parcelize
data class PokemonAbilityModel(
    @SerializedName("ability") val ability: Ability,
) : Parcelable

@Parcelize
data class PokemonStat(
    @SerializedName("base_stat") val hitPoints: String = "",
) : Parcelable

@Parcelize
data class Ability(
    @SerializedName("name") val name: String = "",
) : Parcelable

@Parcelize
data class Moves(
    @SerializedName("move") val move: Move,
) : Parcelable

@Parcelize
data class Move(
    @SerializedName("name") val name: String = "",
) : Parcelable

@Parcelize
data class Specie(
    @SerializedName("name") val name: String = "",
) : Parcelable
