package com.test.di

import android.content.Context
import com.test.network.ApiInterface
import com.google.gson.Gson
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.test.BuildConfig
import com.test.base.EmptyViewModel
import com.test.data.AuthManager
import com.test.data.PreferencesManager
import com.test.ui.MainViewModel
import com.test.ui.MainViewModelImpl
import com.test.ui.login.LoginUseCase
import com.test.ui.login.LoginUseCaseImpl
import com.test.ui.login.LoginViewModel
import com.test.ui.login.LoginViewModelImpl
import com.test.ui.products.ProductUseCase
import com.test.ui.products.ProductUseCaseImpl
import com.test.ui.products.productDetail.FragmentDetailProductArgs
import com.test.ui.products.productDetail.ProductDetailViewModel
import com.test.ui.products.productDetail.ProductDetailViewModelImpl
import com.test.ui.products.productList.ProductListViewModel
import com.test.ui.products.productList.ProductListViewModelImpl
import com.test.ui.profile.ProfileUseCase
import com.test.ui.profile.ProfileUseCaseImpl
import com.test.ui.profile.ProfileViewModel
import com.test.ui.profile.ProfileViewModelImpl
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
    viewModel { EmptyViewModel() }
    viewModel<LoginViewModel> { LoginViewModelImpl(get()) }
    viewModel<MainViewModel> { MainViewModelImpl(get(), get()) }
    viewModel<ProfileViewModel> { ProfileViewModelImpl(get()) }
    viewModel<ProductListViewModel> { ProductListViewModelImpl(get()) }
    viewModel<ProductDetailViewModel> { (args: FragmentDetailProductArgs) ->
        ProductDetailViewModelImpl(args.product, get(), get())
    }
}

private val networkModule = module {
    single(named(TEST_API)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient.Builder>()
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
    single { AuthManager() }
    single { OkHttpClient.Builder() }
}

private val apiModule = module {
    single { get<Retrofit>(named(TEST_API)).create(ApiInterface::class.java) }
}

private val useCaseModule = module {
//    factory { ProofUseCase(get()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get(), get(), get()) }
    factory<ProfileUseCase> { ProfileUseCaseImpl(get()) }
    factory<ProductUseCase> { ProductUseCaseImpl(get(), get()) }
}


val appModules = mutableListOf(viewModelModule, networkModule, dataModule, apiModule, useCaseModule)