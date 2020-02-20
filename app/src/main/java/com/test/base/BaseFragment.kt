package com.test.base

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class BaseFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = context as BaseActivity
    }

    private fun showLoading() {
        baseActivity.showLoading()
    }

    private fun hideLoading() {
        baseActivity.hideLoading()
    }

    fun popBackStack() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun showFragment(fragment: BaseFragment, idContainer: Int, tag: String) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(idContainer, fragment)
            .addToBackStack(tag)
            .commit()
    }

    fun addFragment(fragment: BaseFragment, idContainer: Int, tag: String) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .add(idContainer, fragment)
            .addToBackStack(tag)
            .commit()
    }

    open fun requestPermissionsResult(requestCode: Int, permissions: Array<out String>, resultCodes: IntArray) {}

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
}


