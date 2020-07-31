package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.api.ImageApi
import com.ellen.kakaoimages.api.repository.ImageRepository
import com.ellen.kakaoimages.api.repository.ImageRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    fun provideImageRepository(api: ImageApi): ImageRepository {
        return ImageRepositoryImpl(api)
    }
    single { provideImageRepository(get()) }

}