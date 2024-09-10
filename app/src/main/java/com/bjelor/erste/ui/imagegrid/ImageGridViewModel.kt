package com.bjelor.erste.ui.imagegrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.erste.domain.FlickrResult
import com.bjelor.erste.domain.GetImagesUseCase
import com.bjelor.erste.domain.Image
import com.bjelor.erste.domain.ReloadImagesUseCase
import com.bjelor.erste.domain.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ImageGridViewModel(
    private val onNavigateToImageDetail: (String) -> Unit,
    private val reloadImagesUseCase: ReloadImagesUseCase,
    getImagesUseCase: GetImagesUseCase,
) : ViewModel() {

    enum class LoadingState {
        Loading,
        Refreshing,
    }

    private val imagesResult: Flow<FlickrResult> = getImagesUseCase()
        .onEach {
            loadingState.value = null
        }

    private val isSearchBarOpen: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    private val loadingState: MutableStateFlow<LoadingState?> =
        MutableStateFlow(null)

    private val gridMode: MutableStateFlow<ImageGridUiState.GridMode> =
        MutableStateFlow(ImageGridUiState.GridMode.Grid)

    private val searchText: MutableStateFlow<String> =
        MutableStateFlow("")

    private val searchTags: MutableStateFlow<Set<String>> =
        MutableStateFlow(emptySet())

    val state: StateFlow<ImageGridUiState> = combine(
        imagesResult,
        loadingState,
        isSearchBarOpen,
        gridMode,
        searchText,
        searchTags,
    ) {
            imagesResult,
            loadingState,
            isSearchBarOpen,
            gridMode,
            searchText,
            searchTags,
        ->
        val images = imagesResult.images

        val gridState = when (loadingState) {
            LoadingState.Loading -> ImageGridUiState.GridState.Loading
            LoadingState.Refreshing -> ImageGridUiState.GridState.Refreshing
            null -> when (imagesResult) {
                is FlickrResult.Error -> {
                    // TODO: Better representation of Error state with cached images
                    if (images.isNotEmpty()) {
                        ImageGridUiState.GridState.Loaded
                    } else {
                        ImageGridUiState.GridState.Error
                    }
                }

                is FlickrResult.Success -> {
                    if (images.isNotEmpty()) {
                        ImageGridUiState.GridState.Loaded
                    } else {
                        ImageGridUiState.GridState.Empty
                    }
                }
            }
        }

        ImageGridUiState(
            images,
            gridState,
            isSearchBarOpen,
            gridMode,
            searchText,
            searchTags.toList()
        )
    }
        .stateIn(viewModelScope, SharingStarted.Lazily, ImageGridUiState())

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
        loadingState.value = LoadingState.Refreshing
    }

    fun onReloadClick() {
        reloadImages()
        loadingState.value = LoadingState.Loading
    }

    fun onSearchClick() {
        isSearchBarOpen.value = true
    }

    fun onModeClick() {
        gridMode.value = if (gridMode.value == ImageGridUiState.GridMode.Grid) {
            ImageGridUiState.GridMode.List
        } else {
            ImageGridUiState.GridMode.Grid
        }
    }

    fun onSearchTextChange(value: String) {
        if (value.endsWith(" ")) {
            addTagAndSearch(value.trim())
        } else {
            searchText.value = value
        }
    }

    fun onClearClick() {
        clearSearchText()
    }

    fun onSearchBackClick() {
        isSearchBarOpen.value = false
        clearSearchText()
    }

    fun onSearchConfirm() {
        addTagAndSearch(searchText.value)
        isSearchBarOpen.value = false
    }

    fun onChipClick(value: String) {
        searchTags.update { searchTags -> searchTags - value }
        reloadImages()
        loadingState.value = LoadingState.Refreshing
    }

    private fun addTagAndSearch(value: String) {
        if (value.isNotBlank()) {
            searchTags.update { it + value }
            clearSearchText()
            reloadImages()
            loadingState.value = LoadingState.Refreshing
        }
    }

    private fun clearSearchText() {
        searchText.value = ""
    }

    private fun reloadImages() {
        viewModelScope.launch {
            reloadImagesUseCase(searchTags.value.toList())
        }
    }

}