package com.bjelor.erste.data

import com.bjelor.erste.data.response.FlickrResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getPublicPhotos(@Query("tags") tags: String): Response<FlickrResponse>

}