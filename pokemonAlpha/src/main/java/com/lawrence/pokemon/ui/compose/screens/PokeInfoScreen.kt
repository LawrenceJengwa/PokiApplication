import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.lawrence.pokemon.R
import com.lawrence.pokemon.model.DetailsModel
import com.lawrence.pokemon.ui.ui.theme.YellowBackground
import com.lawrence.pokemon.utils.divideByTen
import com.lawrence.pokemon.viewModel.PokemonStateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeInfoScreen(
    navController: NavController,
    pokemonStateViewModel: PokemonStateViewModel
) {
    val detail = pokemonStateViewModel.detail
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text(text = stringResource(id = R.string.info_title))},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) {
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            InfoDetailContent(
                imageUrl = detail.sprite.imageURL,
                detailsModel = detail
                )
        }
    }
}

@Composable
private fun InfoDetailContent(
    imageUrl: String,
    detailsModel: DetailsModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = detailsModel.name,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
            Text(
                text = detailsModel.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                InfoItem(label = "Weight", value = "${detailsModel.weight.divideByTen()} Kg")
                InfoItem(label = "Height", value = "${detailsModel.height.divideByTen()} m")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                detailsModel.stat.firstOrNull()?.hitPoints?.let { InfoItem(label = "Hit Points", value = it) }
                detailsModel.abilities.firstOrNull()?.ability?.name?.let {
                    InfoItem(label = "Ability", value = it) }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                detailsModel.moves.firstOrNull()?.move?.name?.let {
                    InfoItem(label = "Move", value = it)
                }
                InfoItem(label = "Species", value = detailsModel.species.name)
            }
            HorizontalDivider(
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp),
                thickness = 2.dp
            )
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}
