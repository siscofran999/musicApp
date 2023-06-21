package com.sisco.musicapp.di

import com.sisco.musicapp.data.RemoteDataSource
import com.sisco.musicapp.data.remote.RestApi
import com.sisco.musicapp.data.remote.RestClient
import org.koin.dsl.module

val remoteModule = module {
    single { RestClient() }

    single { RestApi(get()) }

    single { RemoteDataSource(get()) }
}