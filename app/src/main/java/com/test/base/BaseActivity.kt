package com.test.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.router.Router
import com.test.utils.LoadingUiHelper
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    private var progressDialog: LoadingUiHelper.ProgressDialogFragment? = null

    var router: Router? = null
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        viewModel = getViewModel(clazz = getViewModelKClass())
        lifecycle.addObserver(viewModel)
    }

    fun showLoading(){
        if (progressDialog == null){
            progressDialog = LoadingUiHelper.showProgress()
            progressDialog?.show(supportFragmentManager, LoadingUiHelper.ProgressDialogFragment.TAG)
        }
    }

    fun hideLoading(){
        progressDialog?.dismiss()
        progressDialog = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelKClass(): KClass<VM> {
        val actualClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return actualClass.kotlin
    }

    companion object {
        const val LOG_TAG = "myLogs"
    }
}