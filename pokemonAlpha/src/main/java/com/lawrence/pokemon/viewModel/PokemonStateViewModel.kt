package com.lawrence.pokemon.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lawrence.pokemon.model.DetailsModel

class PokemonStateViewModel: ViewModel() {

    var detail by mutableStateOf(DetailsModel())
}