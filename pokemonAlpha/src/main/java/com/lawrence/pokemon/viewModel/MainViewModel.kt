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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val pokemonList = mutableListOf<PokemonItem>()
    private val pokemonDetailsMap = mutableMapOf<String, DetailsModel>()

    private val _uiState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val uiState: StateFlow<ViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getPokemon()
        }
    }

    private suspend fun getPokemon() {
        repository.fetchPokemonList(OFFSET, LIMIT).collect { result ->
            when (result) {
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Result.Success -> {
                    pokemonList.addAll(result.data.results)
                    getDetailsForAllPokemon()
                    _uiState.update { it.copy(isSuccess = true) }
                }

                is Result.Error -> {
                    _uiState.update { it.copy(isError = true) }
                }
            }
        }
    }

    private suspend fun getDetailsForAllPokemon() {
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

data class ViewState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)