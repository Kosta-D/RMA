package com.example.catlist1.breeds.list


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catlist1.core.AppTopBar
import com.example.catlist1.core.LoadingIndicator
import com.example.catlist1.core.NoDataContent
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState


@Composable
fun BreedListScreen(
    viewModel: BreedListViewModel,
    onBreedClick: (String) -> Unit
) {
    val state = viewModel.state.collectAsState()

    BreedListScreen(
        state = state.value,
        eventPublisher = viewModel::setEvent,
        onBreedClick = onBreedClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreedListScreen(
    state: BreedListContract.UiState,
    eventPublisher: (BreedListContract.UiEvent) -> Unit,
    onBreedClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopBar(
                text = "Rase Mačaka",
                navigationIcon = Icons.Default.Refresh,
                navigationOnClick = { eventPublisher(BreedListContract.UiEvent.LoadAll) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Search
            BasicTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    eventPublisher(BreedListContract.UiEvent.Search(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                    ) {
                        if (searchQuery.isEmpty()) Text("Pretrazi rase...")
                        innerTextField()
                    }
                }
            )

            when {
                state.loading -> LoadingIndicator()
                state.error != null -> NoDataContent("Greska: ${state.error.message}")
                state.data.isEmpty() -> NoDataContent("Nema rezultata.")
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.data) { breed ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onBreedClick(breed.id) }
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(breed.name, style = MaterialTheme.typography.titleMedium)

                                breed.alt_names?.takeIf { it.isNotBlank() }?.let {
                                    Text(
                                        text = "Poznato i kao: $it",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Text(
                                    text = breed.description.take(250) + if (breed.description.length > 250) "... " else "",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                val temperamentList = breed.temperament.split(", ", ",").map { it.trim() }.take(5)
                                Row(
                                    modifier = Modifier
                                        .horizontalScroll(rememberScrollState())
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    temperamentList.forEach { trait ->
                                        AssistChip(
                                            onClick = { /* optional */ },
                                            label = { Text(trait) },
                                            colors = AssistChipDefaults.assistChipColors()
                                        )
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