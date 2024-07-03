package com.lawrence.pokiapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lawrence.pokemon.ui.PokeHomeActivity
import com.lawrence.pokiapplication.ui.theme.PokiApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokiApplicationTheme {
                Scaffold(
                    topBar = {
                        pokeAppBar("PokeMon")
                    }
                ) { _ ->
                    LaunchPokemonModule()
                }
            }
        }
    }
}

@Composable
fun LaunchPokemonModule() {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                val intent = Intent(context, PokeHomeActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Launch Pokemon")
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pokeAppBar(title: String) {
    TopAppBar(
        title = {
            Text(title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )
}
