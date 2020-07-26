package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.network.ImageApi
import com.ellen.kakaoimages.network.repository.ImageRepository
import org.koin.dsl.module

val repositoryModule = module {

    fun provideUsersRepository(api: ImageApi): ImageRepository {
        return ImageRepository(api)
    }
    single { provideUsersRepository(get()) }

}