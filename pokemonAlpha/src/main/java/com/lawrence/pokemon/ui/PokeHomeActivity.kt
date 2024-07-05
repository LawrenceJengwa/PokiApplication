package com.lawrence.pokemon.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lawrence.pokemon.ui.compose.PokeNavController
import com.lawrence.pokemon.ui.ui.theme.PokiApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokeHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokiApplicationTheme {
                PokeNavController()
            }
        }
    }
}
