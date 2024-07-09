package com.lawrence.pokemon.repo

import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.model.PokemonListModel
import com.lawrence.pokemon.utils.Result
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun fetchPokemonList(
        offset: Int,
        limit: Int,
    ): Flow<Result<PokemonListModel>>

    suspend fun fetchPokemonDetail(name: String): Flow<Result<DetailsModel>>
}
