package com.bjelor.erste.data

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY_HEADER_NAME = "api_key"
private const val API_KEY_HEADER_VALUE =
    "947cffafcca14f9c854ce1cac7d162d7"

class FlickrApiHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().addHeader(
            name = API_KEY_HEADER_NAME,
            value = API_KEY_HEADER_VALUE,
        ).build()

        return chain.proceed(newRequest)
    }
}
