package com.lawrence.pokemon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.model.PokemonItem
import com.lawrence.pokemon.model.PokemonListModel
import com.lawrence.pokemon.repo.PokemonRepository
import com.lawrence.pokemon.utils.Constants.LIMIT
import com.lawrence.pokemon.utils.Constants.OFFSET
import com.lawrence.pokemon.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    private val _pagingData: MutableStateFlow<PagingData<PokemonItem>> =
        MutableStateFlow(PagingData.empty())
    val pagingData: StateFlow<PagingData<PokemonItem>> get() = _pagingData.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    lateinit var pokemonList: List<PokemonItem>
    lateinit var model: PokemonListModel
    var pokemonItem: DetailsModel = DetailsModel()
    var imageUrl: String = ""

    private val _uiState: MutableStateFlow<PokiUiState> = MutableStateFlow(PokiUiState())
    val uiState: StateFlow<PokiUiState> = _uiState.asStateFlow()

    init {
        fetchPokemonData()
    }

    private fun fetchPokemonData() {
        viewModelScope.launch {
            _uiState.value = PokiUiState(isLoading = true)
            try {
                getPokemon()
                getDetailsForAllPokemon()
                _uiState.value = PokiUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = PokiUiState(isError = true)
            }
        }
    }

    private suspend fun getPokemon() {
        repository.fetchPokemonList(OFFSET, LIMIT).collect { result ->
            when (result) {
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Result.Success -> {
                    pokemonList = result.data.results
                    model = result.data
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
                        pokemonItem = result.data
                        _uiState.update { it.copy(isSuccess = true) }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isError = true) }
                    }
                }
            }
        }
    }


    /* private suspend fun getPokemon() {
     repository.fetchPokemonList(OFFSET, LIMIT).map { result ->
         when (result) {
             is Result.Loading -> {
                 _uiState.value = PokiUiState(isLoading = true)
             }

             is Result.Success -> {
                 pokemonList = result.data.results
                 _uiState.value = PokiUiState(pokiDataState = result.data)
             }

             is Result.Error -> {
                 _uiState.value = PokiUiState(errorMessage = result.errorMessage)
             }
         }
     }.launchIn(viewModelScope)
 }


 suspend fun getDetail() {
     pokemonList.map { pokemonName ->
         repository.fetchPokemonDetail(pokemonName.name).map { result ->
             when (result) {
                 is Result.Loading -> {
                     _uiState.value = PokiUiState(isLoading = true)
                 }

                 is Result.Success -> {
                     _uiState.value = PokiUiState(spriteState = result.data)
                     imageUrl = result.data.sprite.imageURL
                 }

                 is Result.Error -> {
                     _uiState.value = PokiUiState(errorMessage = result.errorMessage)
                 }
             }
         }.launchIn(viewModelScope)
     }
 }
}*/
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredPokemonList(): List<PokemonItem> {
        val query = _searchQuery.value.lowercase()
        return pokemonList.filter { it.name.contains(query, ignoreCase = true) }
    }
}

data class PokiUiState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)