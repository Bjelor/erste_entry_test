package com.bjelor.erste.ui.imagegrid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.bjelor.erste.R
import com.bjelor.erste.domain.Image
import com.bjelor.erste.ui.theme.FlickersteTheme
import com.bjelor.erste.ui.theme.VerticalGradientBlack
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ImageGridScreen(
    onNavigateToImageDetail: (String) -> Unit,
) {
    val viewModel: ImageGridViewModel =
        getViewModel(parameters = { parametersOf(onNavigateToImageDetail) })

    val images = viewModel.images.collectAsState().value
    val gridState = viewModel.gridState
    val onImageClicked = viewModel::onImageClicked
    val onSwipeToRefresh = viewModel::onSwipeToRefresh
    val onReloadClick = viewModel::onReloadClick

    FlickersteTheme {
        Scaffold(
            topBar = {
                SearchAppBar(
                    viewModel.isSearchBarOpen,
                    viewModel.searchText,
                    viewModel::onSearchTextChange,
                    viewModel::onClearClick,
                    viewModel::onSearchBackClick,
                    viewModel::onSearchConfirm,
                    viewModel::onSearchClick,
                )
            },
        ) { paddingValues ->
            when (gridState) {
                ImageGridViewModel.GridState.Loading -> {
                    LoadingScreen(paddingValues)
                }
                ImageGridViewModel.GridState.Loaded,
                ImageGridViewModel.GridState.Refreshing,
                -> {
                    val isRefreshing = gridState == ImageGridViewModel.GridState.Refreshing
                    ImageGrid(paddingValues, images, isRefreshing, onImageClicked, onSwipeToRefresh)
                }
                ImageGridViewModel.GridState.Error -> {
                    ErrorScreen(paddingValues, onReloadClick)
                }
            }
        }
    }
}

@Composable
private fun ImageGrid(
    paddingValues: PaddingValues,
    images: List<Image>,
    isRefreshing: Boolean,
    onImageClicked: (Image) -> Unit,
    onRefresh: () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
    ) {
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
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(64.dp)
                                    .align(Alignment.Center),
                            )
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

@Composable
private fun LoadingScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp),
        )
    }

}

@Composable
private fun ErrorScreen(
    paddingValues: PaddingValues,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.imagegrid_error_title)
        )
        Text(
            text = stringResource(id = R.string.imagegrid_error_description)
        )
        IconButton(onClick = onRefresh) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ImageGridPreview() {
    ImageGrid(
        PaddingValues(),
        listOf(
            Image("", "Something"),
            Image("", "Else"),
            Image("", "Anything"),
        ),
        false,
        {},
        {},
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(
        PaddingValues(),
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    ErrorScreen(
        PaddingValues(),
    ) {}
}