package com.bjelor.erste.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FlickrImageEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String,
)