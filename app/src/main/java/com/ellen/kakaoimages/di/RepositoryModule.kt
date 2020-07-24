package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.network.ImageApi
import com.ellen.kakaoimages.network.repository.ImageRepository
import com.ellen.kakaoimages.network.repository.ImageRepositoryImpl
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideUsersRepository(api: ImageApi, context: Context): ImageRepository {
        return ImageRepositoryImpl(
            api,
            context
        )
    }
    single { provideUsersRepository(get(), androidContext()) }

}