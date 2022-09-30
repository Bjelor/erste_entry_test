package com.bjelor.erste.ui.imagegrid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.erste.domain.FlickrResult
import com.bjelor.erste.domain.GetImagesUseCase
import com.bjelor.erste.domain.Image
import com.bjelor.erste.domain.ReloadImagesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ImageGridViewModel(
    private val onNavigateToImageDetail: (String) -> Unit,
    private val reloadImagesUseCase: ReloadImagesUseCase,
    getImagesUseCase: GetImagesUseCase,
) : ViewModel() {

    enum class GridState {
        Refreshing,
        Loading,
        Loaded,
        Error,
        Empty,
    }

    enum class Mode(val columns: Int) {
        Grid(4),
        List(1),
    }

    val images: StateFlow<List<Image>> = getImagesUseCase()
        .onEach { result ->
            gridState = when (result) {
                is FlickrResult.Error -> {
                    GridState.Error
                }

                is FlickrResult.Success -> {
                    if (result.images.isNotEmpty()) {
                        GridState.Loaded
                    } else {
                        GridState.Empty
                    }
                }
            }
        }
        .map { result ->
            when (result) {
                is FlickrResult.Error -> emptyList()
                is FlickrResult.Success -> result.images
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    var gridState: GridState by mutableStateOf(GridState.Loading)
        private set

    var isSearchBarOpen: Boolean by mutableStateOf(false)
        private set

    var mode: Mode by mutableStateOf(Mode.Grid)
        private set

    var searchText: String by mutableStateOf("")
        private set

    var searchTags: List<String> by mutableStateOf(emptyList())
        private set

    init {
        reloadImages()
    }

    fun onImageClicked(image: Image) {
        val encodedUrl = URLEncoder.encode(
            image.url,
            StandardCharsets.UTF_8.toString(),
        )

        onNavigateToImageDetail(encodedUrl)
    }

    fun onSwipeToRefresh() {
        reloadImages()
        gridState = GridState.Refreshing
    }

    fun onReloadClick() {
        reloadImages()
        gridState = GridState.Loading
    }

    fun onSearchClick() {
        isSearchBarOpen = true
    }

    fun onModeClick() {
        mode = if (mode == Mode.Grid) {
            Mode.List
        } else {
            Mode.Grid
        }
    }

    fun onSearchTextChange(value: String) {
        if (value.endsWith(" ")) {
            addTagAndSearch(value.trim())
        } else {
            searchText = value
        }
    }

    fun onClearClick() {
        clearSearchText()
    }

    fun onSearchBackClick() {
        isSearchBarOpen = false
        clearSearchText()
    }

    fun onSearchConfirm() {
        addTagAndSearch(searchText)
        isSearchBarOpen = false
    }

    fun onChipClicked(value: String) {
        searchTags = searchTags.filterNot { it == value }
        reloadImages()
        gridState = GridState.Refreshing
    }

    private fun addTagAndSearch(value: String) {
        searchTags = searchTags + value
        clearSearchText()
        reloadImages()
        gridState = GridState.Refreshing
    }

    private fun clearSearchText() {
        searchText = ""
    }

    private fun reloadImages() {
        viewModelScope.launch {
            reloadImagesUseCase(searchTags)
        }
    }

}