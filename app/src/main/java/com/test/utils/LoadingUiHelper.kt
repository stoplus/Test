package com.test.utils

import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import kotlin.math.roundToInt

object LoadingUiHelper {

    fun showProgress(): ProgressDialogFragment{
        return ProgressDialogFragment()
    }

    class ProgressDialogFragment : DialogFragment() {

        companion object {
            const val TAG = "ProgressDialogFragment"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            isCancelable = false
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val frameLayout = FrameLayout(context!!)
            var lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            frameLayout.layoutParams = lp

            val progressBar = ProgressBar(context)
            lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.CENTER
            val margin = dpToPx()
            lp.setMargins(margin, margin, margin, margin)
            progressBar.layoutParams = lp

            frameLayout.addView(progressBar)

            return frameLayout
        }

        override fun onStart() {
            super.onStart()

            val window = dialog?.window
            window?.setBackgroundDrawable(null)
        }

        private fun dpToPx(): Int {
            val density = Resources.getSystem().displayMetrics.density
            return (16f * density).roundToInt()
        }
    }
}