package com.bjelor.erste.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.erste.domain.Image
import com.bjelor.erste.domain.LoadImagesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val loadImagesUseCase: LoadImagesUseCase,
) : ViewModel() {

    val images: StateFlow<List<Image>> = flow {
        emit(loadImagesUseCase(listOf("featured")))
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}