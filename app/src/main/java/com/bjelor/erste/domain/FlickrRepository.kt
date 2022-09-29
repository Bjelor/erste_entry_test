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
            runCatching {
                flickrService.getPublicPhotos(tags)
            }.getOrThrow().let {
                imageMapper.from(it)
            }.let {
                flickrLocalCache.updateCache(it)
            }
        }
    }

    fun getImages(): SharedFlow<List<Image>> = flickrLocalCache.imagesCache

}