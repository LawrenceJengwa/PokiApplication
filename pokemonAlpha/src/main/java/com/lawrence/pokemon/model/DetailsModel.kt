package com.lawrence.pokemon.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsModel(
    @SerializedName("name") val name: String = "",
    @SerializedName("weight") val weight: Int = 0,
    @SerializedName("height") val height: Int = 0,
    @SerializedName("species") val species: Specie = Specie(),
    @SerializedName("types") val types: List<PokemonDetailsTypeItemModel> = listOf(),
    @SerializedName("sprites") val sprite: PokemonDetailsSpritesModel = PokemonDetailsSpritesModel(),
    @SerializedName("abilities") val abilities: List<PokemonAbilityModel> = listOf(),
    @SerializedName("stats") val stat: List<PokemonStat> = listOf(),
    @SerializedName("moves") val moves: List<Moves> = listOf()
) : Parcelable

@Parcelize
data class PokemonDetailsTypeModel(
    @SerializedName("name") val name: String
) : Parcelable

@Parcelize
data class PokemonDetailsTypeItemModel(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: PokemonDetailsTypeModel
) : Parcelable

@Parcelize
data class PokemonDetailsSpritesModel(
    @SerializedName("front_default") val imageURL: String = ""
) : Parcelable

@Parcelize
data class PokemonAbilityModel(
    @SerializedName("ability") val ability: Ability
) : Parcelable

@Parcelize
data class PokemonStat(
    @SerializedName("base_stat") val hitPoints: String = ""
) : Parcelable

@Parcelize
data class Ability(
    @SerializedName("name") val name: String = ""
) : Parcelable

@Parcelize
data class Moves(
    @SerializedName("move") val move: Move
) : Parcelable

@Parcelize
data class Move(
    @SerializedName("name") val name: String = ""
) : Parcelable

@Parcelize
data class Specie(
    @SerializedName("name") val name: String = ""
) : Parcelable