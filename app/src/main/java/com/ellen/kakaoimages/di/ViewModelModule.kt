package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.viewmodel.UsersViewModel
import com.ellen.kakaoimages.views.ui.ImageDetailFragment
import com.ellen.kakaoimages.views.ui.ImageSearchFragment
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        UsersViewModel(repository = get())
    }
    fragment {
        ImageSearchFragment()
    }

    fragment {
        ImageDetailFragment()
    }
}