package com.lawrence.pokemon.di

import com.lawrence.pokemon.networking.PokemonService
import com.lawrence.pokemon.repo.PokemonRepoImpl
import com.lawrence.pokemon.repo.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providePokemonRepo(service: PokemonService): PokemonRepository = PokemonRepoImpl(service)
}