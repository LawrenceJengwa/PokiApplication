package com.lawrence.pokemon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonListModel(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonItem> = listOf(),
) : Parcelable
