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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.lawrence.pokemon.ui.PokeHomeActivity
import com.lawrence.pokemon.ui.ui.theme.DarkGreen
import com.lawrence.pokemon.ui.ui.theme.LimeYellow
import com.lawrence.pokiapplication.ui.theme.pokiApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            pokiApplicationTheme {
                Scaffold(
                    topBar = {
                        pokeAppBar(stringResource(id = R.string.pokemon))
                    },
                ) { padding ->
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(padding),
                    ) {
                        launchPokemonModule()
                    }
                }
            }
        }
    }
}

@Composable
fun launchPokemonModule() {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                val intent = Intent(context, PokeHomeActivity::class.java)
                context.startActivity(intent)
            },
            colors =
                ButtonColors(
                    containerColor = LimeYellow,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray,
                ),
        ) {
            Text(
                text = stringResource(id = R.string.launch_poke),
                fontWeight = FontWeight.Bold,
            )
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
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = DarkGreen,
                titleContentColor = Color.White,
            ),
    )
}
