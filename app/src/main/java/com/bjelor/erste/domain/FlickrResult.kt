package com.bjelor.erste.domain

sealed interface FlickrResult {

    val images: List<Image>

    data class Success(override val images: List<Image>) : FlickrResult
    data class Error(override val images: List<Image>, val errorMessage: String? = null) : FlickrResult
}