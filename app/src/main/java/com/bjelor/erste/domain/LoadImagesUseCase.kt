package com.bjelor.erste.domain

class LoadImagesUseCase(private val flickrRepository: FlickrRepository) {

    suspend operator fun invoke(tags: List<String>) = flickrRepository.loadImages(tags)
}