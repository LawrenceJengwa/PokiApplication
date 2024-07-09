package com.lawrence.pokemon.networking

import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.model.PokemonListModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): PokemonListModel

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String,
    ): DetailsModel
}
