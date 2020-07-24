package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.network.ImageApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun provideUsersApi(retrofit: Retrofit): ImageApi {
        return retrofit.create(ImageApi::class.java)
    }
    single { provideUsersApi(get()) }

}