package com.sisco.musicapp.di

import com.sisco.musicapp.data.repository.MainRepository
import com.sisco.musicapp.data.repository.MainRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MainRepository> { MainRepositoryImpl(get()) }
}