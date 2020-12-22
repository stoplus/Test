package com.test.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.test.router.Router
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

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {
    lateinit var binding: VB
    private lateinit var baseActivity: BaseActivity<*, *>
    protected var router: Router? = null
    protected lateinit var viewModel: VM
    private var contentView: View? = null
    lateinit var mContext: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = context as BaseActivity<*, *>
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.router = baseActivity.router
        viewModel.router = router
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBindingClass()

        viewModel = getKoin().getViewModel(
            ViewModelParameters(
                clazz = getViewModelKClass(),
                owner = this,
                parameters = getParameters()
            )
        )

        mContext = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (contentView == null) {
            contentView = binding.root
        }
        return contentView
    }

    protected open fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            showLoading()
        } else {
            hideLoading()
        }
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

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }
}


