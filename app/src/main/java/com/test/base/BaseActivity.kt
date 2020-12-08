package com.test.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.router.Router
import com.test.utils.LoadingUiHelper
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

//    private var currentFragment: BaseFragment? = null
    private var progressDialog: LoadingUiHelper.ProgressDialogFragment? = null

    var router: Router? = null
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        viewModel = getViewModel(clazz = getViewModelKClass())
        lifecycle.addObserver(viewModel)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        currentFragment?.activityResult(requestCode, resultCode, data)
//
//        Log.d(LOG_TAG, "BaseActivity $requestCode $resultCode $currentFragment")
//    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        currentFragment?.requestPermissionsResult(requestCode, permissions, grantResults)
//
//        Log.d(LOG_TAG, "BaseActivity onRequestPermissionsResult $currentFragment")
//    }

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

//    protected fun showFragment(fragment: BaseFragment, idContainer: Int, tag: String){
//        currentFragment = fragment
//        supportFragmentManager
//            .beginTransaction()
//            .replace(idContainer, fragment)
//            .addToBackStack(tag)
//            .commit()
//    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelKClass(): KClass<VM> {
        val actualClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return actualClass.kotlin
    }

    companion object {
        const val LOG_TAG = "myLogs"
    }
}