package com.bjelor.erste.data

import com.bjelor.erste.data.response.FlickrResponse
import com.bjelor.erste.domain.Image

class ImageMapper {

    fun from(flickrResponse: FlickrResponse): List<Image> =
        flickrResponse.items.map { item ->
            Image(item.media.url, item.title)
        }
}