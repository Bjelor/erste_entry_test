package com.bjelor.erste.ui.imagegrid

import com.bjelor.erste.domain.Image

data class ImageGridUiState(
    val images: List<Image> = emptyList(),
    val gridState: GridState = GridState.Loading,
    val isSearchBarOpen: Boolean = false,
    val gridMode: GridMode = GridMode.Grid,
    val searchText: String = "",
    val searchTags: List<String> = emptyList(),
) {
    enum class GridState {
        Refreshing,
        Loading,
        Loaded,
        Error,
        Empty,
    }

    enum class GridMode(val columns: Int) {
        Grid(4),
        List(1),
    }
}
