package com.ellen.kakaoimages

import com.ellen.kakaoimages.di.*
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KakaoImageApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KakaoImageApplication)
            modules(
                apiModule,
                viewModelModule,
                repositoryModule,
                networkModule
            )
        }
    }
}