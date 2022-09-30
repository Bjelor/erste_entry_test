package com.bjelor.erste.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetImageByUrlUseCase(private val flickrRepository: FlickrRepository) {

    operator fun invoke(url: String): Flow<Image> =
        flickrRepository.getFlickrResult().map { result ->
            when (result) {
                is FlickrResult.Error -> throw IllegalStateException("image not found with url $url")
                is FlickrResult.Success -> result.images.find { it.url == url }
                    ?: throw IllegalStateException("image not found with url $url")
            }
        }
}