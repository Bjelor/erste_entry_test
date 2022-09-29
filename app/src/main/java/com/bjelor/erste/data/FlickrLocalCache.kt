package com.bjelor.erste.data

import com.bjelor.erste.domain.Image
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class FlickrLocalCache {

    private val _imagesCache: MutableSharedFlow<List<Image>> = MutableSharedFlow(1)

    val imagesCache: SharedFlow<List<Image>> = _imagesCache

    suspend fun updateCache(newValue: List<Image>) {
        _imagesCache.emit(newValue)
    }
}