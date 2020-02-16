package com.test.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.test.utils.SingleLiveEvent
import org.koin.core.KoinComponent

abstract class BaseViewModel : ViewModel(), LifecycleObserver, KoinComponent {

    val showProgress = SingleLiveEvent<Boolean>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreateView() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroyView() {
    }
}//designloft