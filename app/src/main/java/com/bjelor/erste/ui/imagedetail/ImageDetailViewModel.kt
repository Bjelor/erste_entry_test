package com.bjelor.erste.ui.imagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.erste.domain.GetImageByUrlUseCase
import com.bjelor.erste.domain.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ImageDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getImageByUrlUseCase: GetImageByUrlUseCase,
) : ViewModel() {

    val image: StateFlow<Image?> = initializeImage(savedStateHandle)
        .stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = null)

    private fun initializeImage(savedStateHandle: SavedStateHandle): Flow<Image> {
        val url = checkNotNull(savedStateHandle.get<String>("url"))
        return getImageByUrlUseCase(url)
    }
}