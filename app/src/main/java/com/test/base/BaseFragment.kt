package com.test.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.test.router.Router
import com.test.utils.subscribe
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.Single


import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelParameters
import org.koin.androidx.viewmodel.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    private lateinit var baseActivity: BaseActivity<*>
    protected var router: Router? = null
    protected lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = context as BaseActivity<*>
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.router = baseActivity.router
        viewModel.router = router
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getKoin().getViewModel(
            ViewModelParameters(
                clazz = getViewModelKClass(),
                owner = this,
                parameters = getParameters()
            )
        )
    }

    protected open fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    protected open fun onError(throwable: Throwable) {
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        baseActivity.showLoading()
    }

    private fun hideLoading() {
        baseActivity.hideLoading()
    }

    fun onBackPressed() {
        activity?.onBackPressed()
    }

//    fun showFragment(fragment: BaseFragment, idContainer: Int, tag: String) {
//        requireActivity().supportFragmentManager
//            .beginTransaction()
//            .replace(idContainer, fragment)
//            .addToBackStack(tag)
//            .commit()
//    }
//
//    fun addFragment(fragment: BaseFragment, idContainer: Int, tag: String) {
//        requireActivity().supportFragmentManager
//            .beginTransaction()
//            .add(idContainer, fragment)
//            .addToBackStack(tag)
//            .commit()
//    }

    open fun requestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        resultCodes: IntArray
    ) {}

    open fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    fun <T> subscribe(single: Single<T>, success: (T) -> Unit, error: ((Throwable) -> Unit)? = null) {
        showLoading()
        single
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideLoading() }
            .autoDisposable(scope())
            .subscribe({
                success.invoke(it)
            }, { error?.invoke(it) })
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewModelKClass(): KClass<VM> {
        val actualClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return actualClass.kotlin
    }

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }
}


