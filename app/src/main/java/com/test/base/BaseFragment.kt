package com.test.base

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class BaseFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = context as BaseActivity
    }

    fun showLoading() {
        baseActivity.showLoading()
    }

    fun hideLoading() {
        baseActivity.hideLoading()
    }

    fun popBackStack() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun clearStack() {
        activity?.supportFragmentManager?.also { fm ->
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
        }
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

    protected fun replaceFragmentInIdContent(fragment: BaseFragment) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    protected fun replaceFragmentWithSharedElement(
        fragment: BaseFragment,
        dressingRoomContainer: Int,
        tag: String,
        view: View
    ) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(dressingRoomContainer, fragment)
            .addToBackStack(tag)
            .addSharedElement(view, ViewCompat.getTransitionName(view)!!)
            .commit()
    }

    open fun requestPermissionsResult(requestCode: Int, permissions: Array<out String>, resultCodes: IntArray) {}

    open fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    fun <T> subscribe(single: Single<T>, success: (T) -> Unit, error: ((Throwable) -> Unit)? = null) {
        showLoading()
            single
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(scope())
                .subscribe({
                hideLoading()
                success.invoke(it)
            }, {
                hideLoading()
                error?.invoke(it)
            })
    }


    fun subscribe(single: Completable, success: () -> Unit, error: ((Throwable) -> Unit)? = null) {
        showLoading()
       single
           .observeOn(AndroidSchedulers.mainThread())
           .autoDisposable(scope())
           .subscribe({
            hideLoading()
            success.invoke()
        }, {
            hideLoading()
            error?.invoke(it)
        })
    }
}


