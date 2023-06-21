package com.sisco.musicapp.utils

import android.content.Context
import com.sisco.musicapp.di.remoteModule
import com.sisco.musicapp.di.repositoryModule
import com.sisco.musicapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext

object JavaKoinStarter {
    @JvmStatic
    fun startKoin(context: Context) {
        org.koin.core.context.startKoin {
            androidContext(context)
            modules(repositoryModule, viewModelModule, remoteModule)
        }
    }
}