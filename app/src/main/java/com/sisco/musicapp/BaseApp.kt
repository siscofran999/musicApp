package com.sisco.musicapp

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import com.sisco.musicapp.utils.JavaKoinStarter.startKoin

class BaseApp: Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        turnOffDarkMode()
        startKoin(applicationContext)
    }

    private fun turnOffDarkMode() {
        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } catch (e: Exception) {
            Log.e("TAG", RuntimeException("FAILED TURN OFF DARK MODE").toString())
        }
    }

}