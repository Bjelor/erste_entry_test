package com.bjelor.erste.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickrResponse(
    val items: List<Item>
) {

    @JsonClass(generateAdapter = true)
    data class Item(
        val title: String,
        val media: Media,
        val description: String,
    ) {

        @JsonClass(generateAdapter = true)
        data class Media(
            @Json(name = "m")
            val url: String
        )
    }
}
