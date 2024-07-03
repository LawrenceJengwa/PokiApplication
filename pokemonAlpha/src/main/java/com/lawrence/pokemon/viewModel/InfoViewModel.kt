package com.lawrence.pokemon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.repo.PokemonRepository
import com.lawrence.pokemon.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailUiState> =
        MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _details = MutableStateFlow<DetailsModel?>(null)
    val details: StateFlow<DetailsModel?> get() = _details.asStateFlow()

    private var name: String = ""

    init {
        viewModelScope.launch {
            getDetail()
        }
    }

    suspend fun getDetail() {
        repository.fetchPokemonDetail(name).map { result ->
            when (result) {
                is Result.Loading -> {
                    _uiState.value = DetailUiState(isLoading = true)
                }

                is Result.Success -> {
                    _uiState.value = DetailUiState(detailState = result.data)
                }

                is Result.Error -> {
                    _uiState.value = DetailUiState(errorMessage = result.errorMessage)
                }
            }
        }
    }
}

data class DetailUiState(
    val detailState: DetailsModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)