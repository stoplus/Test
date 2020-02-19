package com.test.base

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.utils.LoadingUiHelper

abstract class BaseActivity : AppCompatActivity() {

    private var currentFragment: BaseFragment? = null
    private var progressDialog: LoadingUiHelper.ProgressDialogFragment? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentFragment?.activityResult(requestCode, resultCode, data)

        Log.d(LOG_TAG, "BaseActivity $requestCode $resultCode $currentFragment")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        currentFragment?.requestPermissionsResult(requestCode, permissions, grantResults)

        Log.d(LOG_TAG, "BaseActivity onRequestPermissionsResult $currentFragment")
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

    protected fun showFragment(fragment: BaseFragment, idContainer: Int, tag: String){
        currentFragment = fragment
        supportFragmentManager
            .beginTransaction()
            .replace(idContainer, fragment)
            .addToBackStack(tag)
            .commit()
    }

    companion object {
        const val LOG_TAG = "myLogs"
    }
}