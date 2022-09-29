package com.bjelor.erste.ui.imagegrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.erste.domain.GetImagesUseCase
import com.bjelor.erste.domain.Image
import com.bjelor.erste.domain.ReloadImagesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ImageGridViewModel(
    private val onNavigateToImageDetail: (String) -> Unit,
    private val reloadImagesUseCase: ReloadImagesUseCase,
    getImagesUseCase: GetImagesUseCase,
) : ViewModel() {

    val images: StateFlow<List<Image>> = getImagesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            reloadImagesUseCase(listOf("featured"))
        }
    }

    fun onImageClicked(image: Image) {
        val encodedUrl = URLEncoder.encode(
            image.url,
            StandardCharsets.UTF_8.toString(),
        )

        onNavigateToImageDetail(encodedUrl)
    }

}