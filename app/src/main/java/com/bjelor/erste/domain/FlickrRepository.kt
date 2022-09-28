package com.bjelor.erste.domain

import com.bjelor.erste.data.FlickrService
import com.bjelor.erste.data.ImageMapper

class FlickrRepository(
    private val flickrService: FlickrService,
    private val imageMapper: ImageMapper,
) {

    suspend fun loadImages(tags: List<String>): List<Image> =
        runCatching {
            flickrService.getPublicPhotos(tags)
        }.getOrThrow().let {
            imageMapper.from(it)
        }
}