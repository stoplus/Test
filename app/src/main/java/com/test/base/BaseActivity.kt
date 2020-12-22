package com.test.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.test.router.Router
import com.test.utils.LoadingUiHelper
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    private var progressDialog: LoadingUiHelper.ProgressDialogFragment? = null

    var router: Router? = null
    protected lateinit var viewModel: VM
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        initViewBindingClass()
        viewModel = getViewModel(clazz = getViewModelKClass())
        lifecycle.addObserver(viewModel)
        setContentView(binding.root)
    }

    fun showLoading() {
        if (progressDialog == null) {
            progressDialog = LoadingUiHelper.showProgress()
            progressDialog?.show(supportFragmentManager, LoadingUiHelper.ProgressDialogFragment.TAG)
        }
    }

    fun hideLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelKClass(): KClass<VM> {
        val actualClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return actualClass.kotlin
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewBindingClass() {
        val actualClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VB>

        val method = actualClass.getMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB
    }
}