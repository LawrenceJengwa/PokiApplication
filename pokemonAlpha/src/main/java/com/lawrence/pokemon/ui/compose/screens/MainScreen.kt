package com.lawrence.pokemon.ui.compose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lawrence.pokemon.R
import com.lawrence.pokemon.ui.compose.views.ProgressView
import com.lawrence.pokemon.ui.compose.views.pokeAppBar
import com.lawrence.pokemon.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredPokemonList by remember(searchQuery) { derivedStateOf { viewModel.getFilteredPokemonList() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Search Pokémon") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        maxLines = 1
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
        when {
            uiState.isLoading -> {
                ProgressView()
            }

            uiState.isSuccess -> {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn {
                        viewModel.model.results.let { results ->
                            items(filteredPokemonList.size) { index ->
                                val item = filteredPokemonList[index]
                                PokemonCell(
                                    index = "${index + 1}",
                                    name = item.name,
                                    url = viewModel.pokemonItem.sprite.imageURL
                                ) {
                                    navController.navigate("details/${item.name}")
                                }
                            }
                        }
                    }
                }

            }
        }
    }
        }
}

@Composable
private fun PokemonCell(
    modifier: Modifier = Modifier,
    index: String,
    name: String,
    url: String,
    onClick: () -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start) {
        Row(
            modifier = Modifier.padding(start = 20.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = index, fontSize = 20.sp)
            Text(text = name, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp))
            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = url,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                error = painterResource(id = R.drawable.ic_person_foreground),
                placeholder = painterResource(id = R.drawable.ic_person_foreground),
            )
        }
        HorizontalDivider(color = Color.Gray, modifier = Modifier.padding(top = 20.dp))
    }
}