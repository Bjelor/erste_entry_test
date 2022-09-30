package com.bjelor.erste.data

import com.bjelor.erste.domain.FlickrResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class FlickrLocalCache {

    private val _imagesCache: MutableSharedFlow<FlickrResult> = MutableSharedFlow(1)

    val imagesCache: SharedFlow<FlickrResult> = _imagesCache

    suspend fun updateCache(newValue: FlickrResult) {
        _imagesCache.emit(newValue)
    }
}