package com.bjelor.erste

import android.app.Application
import com.bjelor.erste.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FlickersteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@FlickersteApplication)
            modules(appModule)
        }
    }
}
