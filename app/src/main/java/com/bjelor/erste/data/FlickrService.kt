package com.bjelor.erste.data

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getPublicPhotos(@Query("tags") tags: List<String>): FlickrResponse

}