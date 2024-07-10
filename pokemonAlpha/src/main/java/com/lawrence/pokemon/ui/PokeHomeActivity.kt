package com.lawrence.pokemon.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.lawrence.pokemon.ui.compose.pokeNavController
import com.lawrence.pokemon.ui.ui.theme.pokeApplicationTheme
import com.lawrence.pokemon.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokeHomeActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.viewModelScope.launch {
            viewModel.fetchPokemonData()
        }

        setContent {
            pokeApplicationTheme {
                pokeNavController(viewModel = viewModel)
            }
        }
    }
}
