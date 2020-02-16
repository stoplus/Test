package com.test.di

import android.content.Context
import com.test.network.ApiInterface
import com.google.gson.Gson
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.test.BuildConfig
import com.test.data.*
import com.test.repository.ModelRepository
import com.test.ui.MainViewModel
import com.test.utils.*
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

private val networkModule = module {
    single(named(MARGINFOX)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient.Builder>()
//                .addInterceptor {
//                    it.proceed(
//                        it.request().newBuilder().url(
//                            it.request().url().newBuilder().addQueryParameter(
//                                "instance",
//                                INSTANCE_MARGINFOX
//                            ).build()
//                        ).build()
//                    )
//                }
                .addInterceptor(get())
                .build())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get())
            .build()
    }
    factory<Interceptor> {
        LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .tag("RETROFIT")
            .build()
    }
}

private val dataModule = module {
    single { get<Context>().applicationContext.getSharedPreferences(PREF, Context.MODE_PRIVATE) }
    single { PreferencesManager(get()) }
    single { ApiManager(get(), get(), get()) }
    single { ModelRepository() }
    factory { TokenAuthenticator(get(), get(), get()) }
    single { OkHttpClient.Builder() }
}

private val apiModule = module {
    single { get<Retrofit>(named(MARGINFOX)).create(ApiInterface::class.java) }
}


val appModules = mutableListOf(viewModelModule, networkModule, dataModule, apiModule)