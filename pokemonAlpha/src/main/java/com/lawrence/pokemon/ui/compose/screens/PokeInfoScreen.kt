package com.lawrence.pokemon.ui.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.viewModel.DetailsViewModel

@Composable
fun PokeInfoScreen(
    navController: NavController,
    name: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val pokemonDetails = viewModel.details.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(pokemonDetails) {
        viewModel.getDetail()
    }
    DetailContent(
        isLoading = uiState.isLoading,
        gotError = !uiState.isLoading,
        pokemonDetails = pokemonDetails.value
    )
}

@Composable
private fun DetailContent(
    isLoading: Boolean,
    gotError: Boolean,
    pokemonDetails: DetailsModel?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(200.dp),
                color = Color.Blue,
                trackColor = Color.Red
            )
        } else if(gotError) {
            //ErrorState()
        } else {
            pokemonDetails.let {details ->
                if (details != null) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = details.sprite.imageURL,
                            contentDescription = "detail",
                            modifier = Modifier
                                .size(40.dp)
                                .weight(1f)
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            text = details.name,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}

