package com.test.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.test.router.Router
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel(), LifecycleObserver {
    protected val viewModelDisposable = CompositeDisposable()
    var router: Router? = null

    protected fun Disposable.untilCleared(): Disposable {
        viewModelDisposable.add(this)
        return this
    }

    protected fun Disposable.cancel() {
        viewModelDisposable.remove(this)
    }

    @CallSuper
    override fun onCleared() {
        viewModelDisposable.clear()
    }
}