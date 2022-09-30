package com.bjelor.erste.domain

sealed interface FlickrResult {
    class Success(val images: List<Image>) : FlickrResult
    object Error : FlickrResult
}