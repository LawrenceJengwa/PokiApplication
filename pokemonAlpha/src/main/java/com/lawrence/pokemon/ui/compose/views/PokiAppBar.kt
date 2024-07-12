package com.lawrence.pokemon.ui.compose.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.lawrence.pokemon.ui.ui.theme.DarkGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pokeAppBar(title: String) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = DarkGreen,
                titleContentColor = Color.White,
            ),
    )
}
