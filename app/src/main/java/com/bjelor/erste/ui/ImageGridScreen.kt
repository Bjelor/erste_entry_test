package com.bjelor.erste.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.bjelor.erste.domain.Image
import org.koin.androidx.compose.getViewModel

@Composable
fun ImageGridScreen() {

    val viewModel: MainViewModel = getViewModel()

    val images = viewModel.images.collectAsState().value

    ImageGrid(images = images)
}

@Composable
fun ImageGrid(images: List<Image>) {

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(images) { image ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = image.url,
                    contentScale = ContentScale.Crop,
                    contentDescription = image.description
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageGridPreview() {
    ImageGrid(
        listOf(
            Image("", ""),
            Image("", ""),
            Image("", ""),
        )
    )
}