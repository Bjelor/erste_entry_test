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

    enum class ScreenState {
        Refreshing,
        Loading,
        Loaded,
        Error,
    }

    val images: StateFlow<List<Image>> = getImagesUseCase()
        .onEach {
            screenState = if (it.isNotEmpty()) {
                ScreenState.Loaded
            } else {
                ScreenState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    var screenState: ScreenState by mutableStateOf(ScreenState.Loading)
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
        screenState = ScreenState.Refreshing
    }

    fun onReloadClick() {
        reloadImages()
        screenState = ScreenState.Loading
    }

    fun onSearchClick() {
        reloadImages()
        screenState = ScreenState.Loading
    }

    private fun reloadImages() {
        viewModelScope.launch {
            reloadImagesUseCase(listOf("featured"))
        }
    }

}