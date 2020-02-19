package com.test

import android.app.Application
import android.util.Log
import com.test.di.appModules
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(this@Application)
            // modules
            modules(appModules)
        }
    }
}