package com.example.catlist1.breeds.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedGalleryScreen(
    viewModel: BreedGalleryViewModel,
    breedId: String,//nepotrebno
    onPhotoClick: (photoId: String) -> Unit,
    onBack: () -> Unit,
) {
    val state = viewModel.state.collectAsState()
    BreedGalleryContent(
        state = state.value,
        onPhotoClick = onPhotoClick,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreedGalleryContent(
    state: BreedGalleryContract.UiState,
    onPhotoClick: (photoId: String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Gallery") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.loading -> {
                        CircularProgressIndicator()
                    }

                    state.photos.isEmpty() -> {
                        Text("No photos available.", style = MaterialTheme.typography.bodyMedium)
                    }

                    else -> {
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            val screenWidth = this.maxWidth
                            val cellSize = (screenWidth / 2) - 4.dp

                            LazyVerticalGrid(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 4.dp),
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {

                                itemsIndexed(
                                    items = state.photos,
                                    key = { _, photo -> photo.photoId },
                                ) { _, photo ->
                                    Card(
                                        modifier = Modifier
                                            .size(cellSize)
                                            .clickable {
                                                onPhotoClick(photo.photoId)
                                            }
                                    ) {
                                        SubcomposeAsyncImage(
                                            modifier = Modifier.fillMaxSize(),
                                            model = photo.imageUrl,
                                            contentDescription = null,
                                        )
                                    }
                                }

                                item(span = { GridItemSpan(2) }) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = 32.dp),
                                        text = "🐾 Meow 🐾",
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
    )
    /*
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Gallery") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {

                    itemsIndexed(
                        items = state.photos,
                        key = { index, photo -> photo.photoId },
                    ) { index, photo ->
                        Card(
                            modifier = Modifier
                                .size(cellSize)
                                .clickable {
                                    onPhotoClick(photo.photoId)
                                }
                        ) {
                            SubcomposeAsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = photo.imageUrl,
                                contentDescription = null,
                            )
                        }
                    }

                    // optional footer
                    item(
                        span = {
                            GridItemSpan(2)
                        }
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 32.dp),
                            text = "🐾 Meow 🐾",
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        },
    )

     */
}
