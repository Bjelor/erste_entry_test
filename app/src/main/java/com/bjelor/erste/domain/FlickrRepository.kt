package com.bjelor.erste.domain

import com.bjelor.erste.data.FlickrLocalCache
import com.bjelor.erste.data.FlickrService
import com.bjelor.erste.data.ImageMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext

class FlickrRepository(
    private val dispatchers: Dispatchers,
    private val flickrService: FlickrService,
    private val flickrLocalCache: FlickrLocalCache,
    private val imageMapper: ImageMapper,
) {

    suspend fun reloadImages(tags: List<String>) {
        withContext(dispatchers.Default) {
            val result = runCatching {
                FlickrResult.Success(
                    flickrService.getPublicPhotos(tags.joinToString()).let { imageMapper.from(it) }
                )
            }.getOrElse {
                FlickrResult.Error
            }

            flickrLocalCache.updateCache(result)
        }
    }

    fun getFlickrResult(): SharedFlow<FlickrResult> = flickrLocalCache.imagesCache

}