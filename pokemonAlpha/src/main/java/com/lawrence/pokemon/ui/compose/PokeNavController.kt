package com.lawrence.pokemon.ui.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lawrence.pokemon.ui.compose.screens.mainScreen
import com.lawrence.pokemon.viewModel.MainViewModel
import com.lawrence.pokemon.viewModel.PokemonStateViewModel
import pokeInfoScreen

@Composable
fun pokeNavController(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel,
) {
    val pokemonStateViewModel: PokemonStateViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            mainScreen(
                navController = navController,
                viewModel = viewModel,
                pokemonStateViewModel = pokemonStateViewModel,
            )
        }

        composable(route = Screen.Info.route) {
            pokeInfoScreen(
                navController = navController,
                pokemonStateViewModel = pokemonStateViewModel,
            )
        }
    }
}
