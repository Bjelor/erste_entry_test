package com.bjelor.erste.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.bjelor.erste.data.entity.FlickrImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlickrImageDao {

    @Transaction
    suspend fun replaceData(images: List<FlickrImageEntity>) {
        deleteAll()
        insertAll(images)
    }

    @Query("SELECT * FROM flickrimageentity")
    fun getAll(): Flow<List<FlickrImageEntity>>

    @Query("SELECT * FROM flickrimageentity WHERE url = :url")
    fun getByUrl(url: String): Flow<FlickrImageEntity>

    @Insert
    suspend fun insertAll(images: List<FlickrImageEntity>)

    @Query("DELETE FROM flickrimageentity")
    suspend fun deleteAll()
}