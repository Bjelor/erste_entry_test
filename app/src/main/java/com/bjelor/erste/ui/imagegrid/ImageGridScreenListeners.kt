package com.bjelor.erste.ui.imagegrid

import com.bjelor.erste.domain.Image

data class ImageGridScreenListeners(
    val appBar: AppBar = AppBar(),
    val grid: Grid = Grid(),
    val onChipClick: (String) -> Unit = {},
) {
    data class AppBar(
        val onSearchTextChange: (String) -> Unit = {},
        val onClearClick: () -> Unit = {},
        val onNavigateBack: () -> Unit = {},
        val onConfirm: () -> Unit = {},
        val onSearchClick: () -> Unit = {},
        val onModeClick: () -> Unit = {},
    )

    data class Grid(
        val onImageClicked: (Image) -> Unit = {},
        val onRefresh: () -> Unit = {},
        val onReloadClick: () -> Unit = {},
    )
}
