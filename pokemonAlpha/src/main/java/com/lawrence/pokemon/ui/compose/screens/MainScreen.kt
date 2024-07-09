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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lawrence.pokemon.R
import com.lawrence.pokemon.ui.compose.Screen
import com.lawrence.pokemon.ui.compose.views.progressView
import com.lawrence.pokemon.ui.ui.theme.LimeYellow
import com.lawrence.pokemon.viewModel.MainViewModel
import com.lawrence.pokemon.viewModel.PokemonStateViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScreen(
    navController: NavController,
    viewModel: MainViewModel,
    pokemonStateViewModel: PokemonStateViewModel,
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
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondary),
                        singleLine = true,
                        maxLines = 1,
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    progressView()
                }

                uiState.isSuccess -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                ),
                    ) {
                        Text(
                            text = stringResource(id = R.string.main_title),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    if (filteredPokemonList.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.not_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red,
                            modifier =
                                Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally),
                        )
                    } else {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .border(
                                        BorderStroke(2.dp, LimeYellow),
                                        shape =
                                            RoundedCornerShape(
                                                topEnd = 4.dp,
                                                bottomEnd = 4.dp,
                                                topStart = 4.dp,
                                                bottomStart = 4.dp,
                                            ),
                                    ),
                        ) {
                            LazyColumn {
                                items(filteredPokemonList.size) { index ->
                                    val item = filteredPokemonList[index]
                                    pokemonListItem(
                                        index = "${index + 1}",
                                        name =
                                            item.name.replaceFirstChar {
                                                it.titlecase()
                                            },
                                        url = item.sprite.imageURL,
                                    ) {
                                        pokemonStateViewModel.detail = item
                                        navController.navigate(Screen.Info.route)
                                    }
                                }
                            }
                        }
                    }
                }

                uiState.isError -> {
                    onError(viewModel)
                }
            }
        }
    }
}

@Composable
private fun pokemonListItem(
    modifier: Modifier = Modifier,
    index: String,
    name: String,
    url: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = index,
            fontStyle = FontStyle.Italic,
            fontSize = 20.sp,
        )
        Text(
            text = name,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(start = 16.dp),
        )
        Box(
            modifier =
                Modifier
                    .size(60.dp)
                    .weight(1f),
            contentAlignment = Alignment.TopEnd,
        ) {
            AsyncImage(
                modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(LimeYellow),
                model = url,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                error = painterResource(id = R.drawable.ic_person_foreground),
                placeholder = painterResource(id = R.drawable.ic_person_foreground),
            )
        }
    }
    HorizontalDivider(
        color = LimeYellow,
        modifier = Modifier.padding(16.dp),
        thickness = 1.dp,
    )
}

@Composable
fun onError(viewModel: MainViewModel) {
    Text(
        text = stringResource(id = R.string.service_error),
        color = Color.Red,
        modifier = Modifier.padding(bottom = 10.dp),
        fontSize = 16.sp,
    )

    Button(onClick = {
        viewModel.viewModelScope.launch {
            viewModel.getPokemon()
        }
    }) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = stringResource(id = R.string.retry),
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(R.string.retry),
            fontWeight = FontWeight.Bold,
        )
    }
}
