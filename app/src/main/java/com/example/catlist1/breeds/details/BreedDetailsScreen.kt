package com.example.catlist1.breeds.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.catlist1.core.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    viewModel: BreedDetailsViewModel,
    breedId: String,
    onBack: () -> Unit,
    onOpenGallery: (breedId: String) -> Unit
) {

    val state = viewModel.breed.collectAsState()

    LaunchedEffect(breedId) {
        viewModel.loadBreedById(breedId)

    }

    val breed = state.value
    val context = LocalContext.current

    if (breed == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            AppTopBar(
                text = breed.name,
                navigationOnClick = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val imageUrl = breed.reference_image_id?.let {
                "https://cdn2.thecatapi.com/images/$it.jpg"
            } ?: "https://via.placeholder.com/300x200?text=No+Image"

            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Opis:", fontWeight = FontWeight.Bold)
            Text(breed.description)

            Spacer(modifier = Modifier.height(8.dp))
            Text("Zemlja porekla: ${breed.origin}")
            Text("Životni vek: ${breed.life_span} godina")
            Text("Težina: ${breed.weight.metric} kg")
            Text("Temperament: ${breed.temperament}")

            Spacer(modifier = Modifier.height(16.dp))
            Text("Osobine:", fontWeight = FontWeight.Bold)

            val osobine = listOf(
                "Adaptability" to breed.adaptability,
                "Affection" to breed.affection_level,
                "Child friendly" to breed.child_friendly,
                "Dog friendly" to breed.dog_friendly,
                "Energy" to breed.energy_level,
                "Grooming" to breed.grooming,
                "Health issues" to breed.health_issues,
                "Intelligence" to breed.intelligence,
                "Shedding" to breed.shedding_level,
                "Social needs" to breed.social_needs,
                "Stranger friendly" to breed.stranger_friendly,
                "Vocalisation" to breed.vocalisation
            )

            osobine.forEach { (label, value) ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = label, style = MaterialTheme.typography.bodyMedium)
                    LinearProgressIndicator(
                        progress = value / 5f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                    Text(text = "$value/5", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Retka vrsta: ${if (breed.rare == 1) "Da" else "Ne"}")

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(breed.wikipedia_url))
                context.startActivity(intent)
            }) {
                Text("Pogledaj na Vikipediji")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                onOpenGallery(breed.id)
            }) {
                Text("Otvori galeriju")
            }
        }
    }
}
