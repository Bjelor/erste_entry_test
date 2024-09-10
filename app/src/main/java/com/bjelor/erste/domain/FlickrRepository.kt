package com.bjelor.erste.domain

import com.bjelor.erste.data.FlickrImageDao
import com.bjelor.erste.data.FlickrService
import com.bjelor.erste.data.entity.FlickrImageEntity
import com.bjelor.erste.data.response.FlickrResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FlickrRepository(
    private val dispatchers: Dispatchers,
    private val flickrService: FlickrService,
    private val flickrImageDao: FlickrImageDao,
) {

    private val networkErrorBody: MutableSharedFlow<String?> = MutableSharedFlow()

    suspend fun reloadImages(tags: List<String>) {
        withContext(dispatchers.Default) {
            kotlin.runCatching {
                val response = flickrService.getPublicPhotos(tags.joinToString())
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    networkErrorBody.emit(response.errorBody()?.string())
                } else {
                    networkErrorBody.emit(null)
                    flickrImageDao.replaceData(body.toEntities())
                }
            }.onFailure { error ->
                networkErrorBody.emit(error.message)
            }
        }
    }

    val flickrResult: Flow<FlickrResult> = combine(
        flickrImageDao.getAll(),
        networkErrorBody,
    ) { imageEntities, errorMessage ->
        val images = imageEntities.map { it.toDomain() }

        if (errorMessage != null) {
            FlickrResult.Error(images, errorMessage)
        } else {
            FlickrResult.Success(images)
        }
    }

    fun getImageByUrl(url: String): Flow<Image> = flickrImageDao.getByUrl(url).map { it.toDomain() }

    private fun FlickrResponse.toEntities(): List<FlickrImageEntity> = items.map { item ->
        FlickrImageEntity(
            url = item.media.url,
            title = item.title,
            description = item.description,
        )
    }

    private fun FlickrImageEntity.toDomain(): Image =
        Image(
            url = url,
            title = title,
            description = description,
        )

}