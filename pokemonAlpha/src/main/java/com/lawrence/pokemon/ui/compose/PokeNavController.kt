package com.lawrence.pokemon.ui.compose

import PokeInfoScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lawrence.pokemon.ui.compose.screens.MainScreen
import com.lawrence.pokemon.viewModel.SharedViewModel

@Composable
fun PokeNavController(navController: NavHostController = rememberNavController()) {

    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(route = Screen.Info.route) {
            PokeInfoScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }
}

