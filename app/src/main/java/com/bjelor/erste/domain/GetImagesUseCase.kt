package com.bjelor.erste.domain

import kotlinx.coroutines.flow.Flow

class GetImagesUseCase(private val flickrRepository: FlickrRepository) {

    operator fun invoke(): Flow<FlickrResult> = flickrRepository.flickrResult
}