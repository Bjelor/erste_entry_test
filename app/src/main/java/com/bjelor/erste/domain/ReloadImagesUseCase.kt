package com.bjelor.erste.domain

class ReloadImagesUseCase(private val flickrRepository: FlickrRepository) {

    suspend operator fun invoke(tags: List<String>) = flickrRepository.reloadImages(tags)
}