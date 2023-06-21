package com.sisco.musicapp.di

import com.sisco.musicapp.ui.MainViewModel
import com.sisco.musicapp.utils.RxDisposer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { RxDisposer() }

    viewModel {
        MainViewModel(get(), get())
    }
}