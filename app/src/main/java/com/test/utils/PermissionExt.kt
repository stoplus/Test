package com.test.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Fragment.withAllPermissions(vararg permissions: String, granted: () -> Unit) {
    val activity = activity ?: return
    Dexter.withActivity(activity)
        .withPermissions(permissions.toList())
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.areAllPermissionsGranted()?.also {
                    if (it) { granted() }
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                token?.continuePermissionRequest()
            }
        })
        .check()
}

fun Activity.withAllPermissions(vararg permissions: String, granted: () -> Unit) {
    Dexter.withActivity(this)
        .withPermissions(permissions.toList())
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.areAllPermissionsGranted()?.also {
                    if (it) { granted() }
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                token?.continuePermissionRequest()
            }
        })
        .check()
}