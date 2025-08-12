package com.example.catlist1.breeds.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.catlist1.breeds.gallery.BreedGalleryContract
import com.example.catlist1.breeds.gallery.BreedGalleryViewModel
//import androidx.compose.foundation.pager.HorizontalPagerDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedPhotoViewerScreen(
    viewModel: BreedGalleryViewModel,
    initialPhotoId: String,
    onClose: () -> Unit,
){
    val state = viewModel.state.collectAsState()
    BreedPhotoViewerContent(
        state = state.value,
        initialPhotoId = initialPhotoId,
        onClose = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class,ExperimentalFoundationApi::class)
@Composable
private fun BreedPhotoViewerContent(
    state: BreedGalleryContract.UiState,
    initialPhotoId: String,
    onClose: () -> Unit,
){
    val pagerState = rememberPagerState(
        initialPage = state.photos.indexOfFirst { it.photoId == initialPhotoId }.coerceAtLeast(0),
        pageCount = { state.photos.size }
    )

    var currentTitle by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentTitle,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        content = { paddingValues ->
            if (state.photos.isNotEmpty()) {
                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
                        currentTitle = "Photo ${pageIndex + 1} / ${state.photos.size}"
                    }
                }

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = { state.photos[it].photoId }
                ) { pageIndex ->
                    val photo = state.photos[pageIndex]

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = photo.imageUrl,
                            contentDescription = null
                        )
                    }
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No photos.",
                    textAlign = TextAlign.Center
                )
            }
        }
    )

}