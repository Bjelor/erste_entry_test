package com.bjelor.erste.di

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
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    viewModel { ImageGridViewModel(get(), get(), get()) }
    viewModel { parametersHolder -> ImageDetailViewModel(parametersHolder.get(), get()) }

    single { RetrofitFactory().create() }

    factory { ImageMapper() }

    factory { GetImageByUrlUseCase(get()) }
    factory { GetImagesUseCase(get()) }
    factory { ReloadImagesUseCase(get()) }

    single { FlickrRepository(get(), get(), get(), get()) }

    single { FlickrLocalCache() }

    factory { get<Retrofit>().create(FlickrService::class.java) }

    single { Dispatchers }
}
