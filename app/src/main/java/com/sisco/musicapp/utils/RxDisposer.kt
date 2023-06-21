package com.sisco.musicapp.utils

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

// custom disposable rxjava with design pattern mvvm
class RxDisposer : DefaultLifecycleObserver {
    private var compositeDisposable: CompositeDisposable? = null

    fun bind(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        compositeDisposable = CompositeDisposable()
    }

    fun add(disposable: Disposable) {
        compositeDisposable?.add(disposable) ?: run {
            throw NullPointerException("No lifecycle bound")
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d("RxDisposer", "ON DESTROY")
        compositeDisposable?.dispose()
        super.onDestroy(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d("RxDisposer", "ON PAUSE")
        compositeDisposable?.clear()
        super.onPause(owner)
    }
}

fun Disposable.addToDisposer(rxDisposer: RxDisposer) {
    rxDisposer.add(this)
}