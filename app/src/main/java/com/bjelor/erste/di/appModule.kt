package com.bjelor.erste.di

import com.bjelor.erste.data.FlickrApiHeaderInterceptor
import com.bjelor.erste.data.FlickrLocalCache
import com.bjelor.erste.data.FlickrService
import com.bjelor.erste.data.ImageMapper
import com.bjelor.erste.data.RetrofitFactory
import com.bjelor.erste.domain.FlickrRepository
import com.bjelor.erste.domain.GetImageByUrlUseCase
import com.bjelor.erste.domain.GetImagesUseCase
import com.bjelor.erste.domain.ReloadImagesUseCase
import com.bjelor.erste.ui.imagedetail.ImageDetailViewModel
import com.bjelor.erste.ui.imagegrid.ImageGridViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    viewModelOf(::ImageGridViewModel)
    viewModel { parametersHolder -> ImageDetailViewModel(parametersHolder.get(), get()) }

    single(qualifier = RetrofitInjection.Flickr.qualifier) {
        RetrofitFactory().create(
            RetrofitInjection.Flickr.baseUrl,
            FlickrApiHeaderInterceptor(),
        )
    }

    factory { ImageMapper() }

    factory { GetImageByUrlUseCase(get()) }
    factory { GetImagesUseCase(get()) }
    factory { ReloadImagesUseCase(get()) }

    single { FlickrRepository(get(), get(), get(), get()) }

    single { FlickrLocalCache() }

    factory { get<Retrofit>(RetrofitInjection.Flickr.qualifier).create(FlickrService::class.java) }

    single { Dispatchers }
}
