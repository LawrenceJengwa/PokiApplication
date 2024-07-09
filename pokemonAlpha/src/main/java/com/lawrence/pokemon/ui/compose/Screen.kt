package com.lawrence.pokemon.ui.compose

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")

    object Info : Screen(route = "Info")
}
