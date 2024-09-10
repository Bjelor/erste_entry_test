package com.bjelor.erste.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bjelor.erste.data.entity.FlickrImageEntity

@Database(entities = [FlickrImageEntity::class], exportSchema = false, version = 1)
abstract class FlickrDatabase : RoomDatabase() {
    abstract fun flickrImageDao(): FlickrImageDao
}