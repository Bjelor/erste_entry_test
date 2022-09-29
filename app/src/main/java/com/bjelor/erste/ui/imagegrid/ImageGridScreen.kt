package com.bjelor.erste.ui.imagegrid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.SubcomposeAsyncImage
import com.bjelor.erste.R
import com.bjelor.erste.domain.Image
import com.bjelor.erste.ui.theme.FlickersteTheme
import com.bjelor.erste.ui.theme.VerticalGradientBlack
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ImageGridScreen(
    onNavigateToImageDetail: (String) -> Unit,
) {
    val viewModel: ImageGridViewModel =
        getViewModel(parameters = { parametersOf(onNavigateToImageDetail) })

    val images = viewModel.images.collectAsState().value

    ImageGrid(viewModel::onImageClicked, images)
}

@Composable
fun ImageGrid(
    onImageClicked: (Image) -> Unit,
    images: List<Image>,
) {
    FlickersteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                )
            },
        ) { paddingValues ->
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                columns = GridCells.Fixed(3),
            ) {
                items(images) { image ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { onImageClicked(image) },
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            model = image.url,
                            loading = {
                                CircularProgressIndicator()
                            },
                            contentScale = ContentScale.Crop,
                            contentDescription = image.title,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Brush.verticalGradient(VerticalGradientBlack))
                                .align(Alignment.BottomCenter),
                            text = image.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageGridPreview() {
    ImageGrid(
        {},
        listOf(
            Image("", "Something"),
            Image("", "Else"),
            Image("", "Anything"),
        ),
    )
}