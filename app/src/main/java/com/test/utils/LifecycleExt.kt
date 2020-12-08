package com.test.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> Fragment.subscribe(liveData: (LiveData<T>)?, onNext: (t: T) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer {
        if (it != null) {
            onNext(it)
        }
    })
}

fun <T> Fragment.subscribeNullable(liveData: LiveData<T>, onNext: (t: T?) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer { onNext(it) })
}

fun <T> AppCompatActivity.subscribe(liveData: (LiveData<T>)?, onNext: (t: T) -> Unit) {
    liveData?.observe(this, Observer {
        if (it != null) {
            onNext(it)
        }
    })
}

fun <T> AppCompatActivity.subscribeNullable(liveData: LiveData<T>, onNext: (t: T?) -> Unit) {
    liveData.observe(this, Observer { onNext(it) })
}

fun <T> DialogFragment.subscribe(liveData: (LiveData<T>)?, onNext: (t: T) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer {
        if(it != null) {
            onNext(it)
        }
    })
}