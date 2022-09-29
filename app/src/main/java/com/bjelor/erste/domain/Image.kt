package com.bjelor.erste.domain

import java.io.Serializable

data class Image(
    val url: String,
    val title: String,
) : Serializable