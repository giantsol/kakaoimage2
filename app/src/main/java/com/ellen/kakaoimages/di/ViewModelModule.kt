package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.viewmodel.ImageViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        ImageViewModel(repository = get())
    }
}