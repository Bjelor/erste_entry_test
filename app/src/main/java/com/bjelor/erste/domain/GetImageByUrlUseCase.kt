package com.bjelor.erste.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetImageByUrlUseCase(private val flickrRepository: FlickrRepository) {

    operator fun invoke(url: String): Flow<Image> =
        flickrRepository.getImages().map { images ->
            images.find { it.url == url }
                ?: throw IllegalStateException("image not found with url $url")
        }
}