package com.bjelor.erste.domain

import kotlinx.coroutines.flow.SharedFlow

class GetImagesUseCase(private val flickrRepository: FlickrRepository) {

    operator fun invoke(): SharedFlow<FlickrResult> = flickrRepository.getFlickrResult()
}