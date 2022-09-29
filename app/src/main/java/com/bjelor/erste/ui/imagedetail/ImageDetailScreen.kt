package com.bjelor.erste.ui.imagedetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.bjelor.erste.domain.Image
import com.bjelor.erste.ui.theme.FlickersteTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun ImageDetailScreen(onNavigateBack: () -> Unit) {

    val viewModel: ImageDetailViewModel = getViewModel()

    val image = viewModel.image.collectAsState().value

    image?.let { ImageDetail(onNavigateBack, it) }
}


@Composable
fun ImageDetail(
    onNavigateBack: () -> Unit,
    image: Image,
) {
    FlickersteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = image.title)
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
                    }
                )
            },
        ) { paddingValues ->
            SubcomposeAsyncImage(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                model = image.url,
                loading = {
                    CircularProgressIndicator()
                },
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
        }
    }
}
