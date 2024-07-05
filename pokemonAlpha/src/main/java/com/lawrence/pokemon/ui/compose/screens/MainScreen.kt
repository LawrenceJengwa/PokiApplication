package com.lawrence.pokemon.ui.compose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lawrence.pokemon.R
import com.lawrence.pokemon.ui.compose.Screen
import com.lawrence.pokemon.ui.compose.views.ProgressView
import com.lawrence.pokemon.ui.ui.theme.Purple80
import com.lawrence.pokemon.viewModel.MainViewModel
import com.lawrence.pokemon.viewModel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
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
                        placeholder = { Text(stringResource(id = R.string.search_label)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary),
                        singleLine = true,
                        maxLines = 1
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    ProgressView()
                }

                uiState.isSuccess -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background)
                    ) {
                        Text(
                            text = stringResource(id = R.string.main_title),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    if (filteredPokemonList.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.not_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(
                                        topEnd = 4.dp,
                                        bottomEnd = 4.dp,
                                        topStart = 4.dp,
                                        bottomStart = 4.dp
                                    )
                                )
                        ) {
                            LazyColumn {
                                items(filteredPokemonList.size) { index ->
                                    val item = filteredPokemonList[index]
                                    PokemonListItem(
                                        index = "${index + 1}",
                                        name = item.name.replaceFirstChar {
                                            it.titlecase()
                                        },
                                        url = item.sprite.imageURL,
                                    ) {
                                        sharedViewModel.detail = item
                                        navController.navigate(Screen.Info.route)
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
private fun PokemonListItem(
    modifier: Modifier = Modifier,
    index: String,
    name: String,
    url: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = index, fontSize = 20.sp)
        Text(text = name, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp))
        Box(
            modifier = Modifier
                .size(60.dp)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Purple80),
                model = url,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                error = painterResource(id = R.drawable.ic_person_foreground),
                placeholder = painterResource(id = R.drawable.ic_person_foreground),
            )
        }
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(16.dp),
        thickness = 2.dp
    )
}