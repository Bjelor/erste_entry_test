package com.bjelor.erste.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

sealed interface RetrofitInjection {
    val qualifier: Qualifier
    val baseUrl: String

    object Flickr : RetrofitInjection {
        override val qualifier = object : Qualifier {
            override val value: QualifierValue
                get() = "FlickrRetrofitQualifier"
        }
        override val baseUrl: String
            get() = "https://www.flickr.com/services/rest/"
    }
}