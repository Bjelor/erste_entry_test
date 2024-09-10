package com.bjelor.erste.domain

import kotlinx.coroutines.flow.Flow

class GetImageByUrlUseCase(private val flickrRepository: FlickrRepository) {

    operator fun invoke(url: String): Flow<Image> = flickrRepository.getImageByUrl(url)
}