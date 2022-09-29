package com.bjelor.erste.ui.imagegrid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.erste.domain.GetImagesUseCase
import com.bjelor.erste.domain.Image
import com.bjelor.erste.domain.ReloadImagesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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
    }

    val images: StateFlow<List<Image>> = getImagesUseCase()
        .onEach {
            gridState = if (it.isNotEmpty()) {
                GridState.Loaded
            } else {
                GridState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    var gridState: GridState by mutableStateOf(GridState.Loading)
        private set

    var isSearchBarOpen: Boolean by mutableStateOf(false)
        private set

    var searchText: String by mutableStateOf("")
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

    fun onSearchTextChange(value: String) {
        searchText = value
    }

    fun onClearClick() {
        searchText = ""
    }

    fun onSearchBackClick() {
        isSearchBarOpen = false
        searchText = ""
    }

    fun onSearchConfirm() {
        reloadImages()
        isSearchBarOpen = false
        gridState = GridState.Refreshing
    }

    private fun reloadImages() {
        viewModelScope.launch {
            reloadImagesUseCase(searchText.split(","))
        }
    }

}