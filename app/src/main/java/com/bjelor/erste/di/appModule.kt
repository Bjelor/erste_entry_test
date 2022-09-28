package com.bjelor.erste.di

import com.bjelor.erste.data.FlickrService
import com.bjelor.erste.data.ImageMapper
import com.bjelor.erste.data.RetrofitFactory
import com.bjelor.erste.domain.FlickrRepository
import com.bjelor.erste.domain.LoadImagesUseCase
import com.bjelor.erste.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val appModule = module {

    viewModel { MainViewModel(get()) }

    single { RetrofitFactory().create() }

    factory { ImageMapper() }

    factory { LoadImagesUseCase(get()) }

    single { FlickrRepository(get(), get()) }

    factory { get<Retrofit>().create(FlickrService::class.java) }

    single { Dispatchers }
}
