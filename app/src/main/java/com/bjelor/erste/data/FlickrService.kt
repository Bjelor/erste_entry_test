package com.bjelor.erste.data

import com.bjelor.erste.data.response.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getPublicPhotos(@Query("tags") tags: String): FlickrResponse

    @GET("/services/oauth/request_token")
    suspend fun requestToken(@Query("tags") tags: String): FlickrResponse

}