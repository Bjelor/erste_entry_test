package com.bjelor.erste.ui.imagegrid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Chip
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageGridScreen(
    onNavigateToImageDetail: (String) -> Unit,
) {
    val viewModel: ImageGridViewModel =
        getViewModel(parameters = { parametersOf(onNavigateToImageDetail) })

    val state = viewModel.state.collectAsState().value
    val onImageClicked = viewModel::onImageClicked
    val onSwipeToRefresh = viewModel::onSwipeToRefresh
    val onReloadClick = viewModel::onReloadClick

    FlickersteTheme {
        Scaffold(
            topBar = {
                SearchAppBar(
                    state.isSearchBarOpen,
                    state.searchText,
                    viewModel::onSearchTextChange,
                    viewModel::onClearClick,
                    viewModel::onSearchBackClick,
                    viewModel::onSearchConfirm,
                    viewModel::onSearchClick,
                    viewModel::onModeClick,
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    items(state.searchTags) { tag ->
                        Chip(onClick = { viewModel.onChipClicked(tag) }) {
                            Text(text = tag)
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                        }
                    }
                }

                when (state.gridState) {
                    ImageGridUiState.GridState.Loading -> {
                        LoadingScreen(paddingValues)
                    }

                    ImageGridUiState.GridState.Loaded,
                    ImageGridUiState.GridState.Refreshing,
                    -> {
                        val isRefreshing = state.gridState == ImageGridUiState.GridState.Refreshing
                        ImageGrid(
                            paddingValues,
                            state.gridMode,
                            state.images,
                            isRefreshing,
                            onImageClicked,
                            onSwipeToRefresh
                        )
                    }

                    ImageGridUiState.GridState.Error -> {
                        ErrorScreen(paddingValues, onReloadClick)
                    }

                    ImageGridUiState.GridState.Empty -> {
                        EmptyScreen(paddingValues)
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageGrid(
    paddingValues: PaddingValues,
    mode: ImageGridUiState.GridMode,
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
            columns = GridCells.Fixed(mode.columns),
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
                                    .wrapContentSize()
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

@Composable
private fun EmptyScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.imagegrid_empty_title)
        )
        Text(
            text = stringResource(id = R.string.imagegrid_empty_description)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageGridPreview() {
    ImageGrid(
        PaddingValues(),
        ImageGridUiState.GridMode.Grid,
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

@Preview(showBackground = true)
@Composable
fun EmptyScreenPreview() {
    EmptyScreen(
        PaddingValues(),
    )
}