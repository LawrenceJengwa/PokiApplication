package com.lawrence.pokemon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.model.PokemonItem
import com.lawrence.pokemon.repo.PokemonRepository
import com.lawrence.pokemon.utils.Constants.LIMIT
import com.lawrence.pokemon.utils.Constants.OFFSET
import com.lawrence.pokemon.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) : ViewModel() {
        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery

        val pokemonList = mutableListOf<PokemonItem>()
        val pokemonDetailsMap = mutableMapOf<String, DetailsModel>()

        private val _uiState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
        var uiState: StateFlow<ViewState> = _uiState.asStateFlow()

        suspend fun fetchPokemonData() {
            try {
                val pokemonFetchJob =
                    viewModelScope.async {
                        getPokemon()
                    }
                pokemonFetchJob.await()

                getPokemonDetails()
            } catch (e: Exception) {
                _uiState.update { it.copy(isError = true) }
            }
        }

        suspend fun getPokemon() {
            repository.fetchPokemonList(OFFSET, LIMIT).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Result.Success -> {
                        populateList(result.data.results)
                        _uiState.update { it.copy(isSuccess = true) }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isError = true) }
                    }
                }
            }
        }

        suspend fun getPokemonDetails() {
            pokemonList.forEach { pokemon ->
                repository.fetchPokemonDetail(pokemon.name).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is Result.Success -> {
                            pokemonDetailsMap[pokemon.name] = result.data
                            if (pokemonDetailsMap.size == pokemonList.size) {
                                _uiState.value = ViewState(isSuccess = true)
                            }
                        }

                        is Result.Error -> {
                            _uiState.update { it.copy(isError = true) }
                        }
                    }
                }
            }
        }

        fun populateList(pokeList: List<PokemonItem>?) {
            pokeList?.takeIf { it.isNotEmpty() }?.let {
                pokemonList.addAll(it)
            }
        }

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
        }

        fun getFilteredPokemonList(): List<DetailsModel> {
            return if (_searchQuery.value.isEmpty()) {
                pokemonList.mapNotNull { pokemonDetailsMap[it.name] }
            } else {
                pokemonList.filter { it.name.contains(_searchQuery.value, true) }
                    .mapNotNull { pokemonDetailsMap[it.name] }
            }
        }
    }
