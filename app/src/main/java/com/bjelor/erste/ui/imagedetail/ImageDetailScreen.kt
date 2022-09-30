package com.bjelor.erste.ui.imagedetail

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.SubcomposeAsyncImage
import com.bjelor.erste.domain.Image
import com.bjelor.erste.ui.theme.FlickersteTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun ImageDetailScreen(
    onShare: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val viewModel: ImageDetailViewModel = getViewModel()

    val image = viewModel.image.collectAsState().value

    ImageDetail(image, onNavigateBack, onShare)
}


@Composable
fun ImageDetail(
    image: Image?,
    onNavigateBack: () -> Unit,
    onShareClick: (String) -> Unit,
) {
    val scale = remember { mutableStateOf(1f) }

    FlickersteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = image?.title ?: "")
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { onShareClick(image?.title ?: "") }) {
                            Icon(Icons.Filled.Share, null)
                        }
                    }
                )
            },
        ) { paddingValues ->
            image?.let { image ->
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    scale.value = if (scale.value > 1f) 1f else 1.5f
                                }
                            )
                        }
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                        ),
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
}

@Preview(showBackground = true)
@Composable
fun ImageDetailPreview() {
    ImageDetail(
        null,
        {},
        {}
    )
}