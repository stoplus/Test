package com.test.utils

import android.content.Context
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.test.R
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun String.toFullDate(): String? {
    try {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .parse(this)
        val fmtOut = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return if (date == null) {
            ""
        } else {
            fmtOut.format(date)
        }

    } catch (e: java.lang.Exception) {
        val newDate = this.replace("Z", "")
        val date1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .parse(newDate)
        val fmtOut1 = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        return if (date1 == null) {
            ""
        } else {
            fmtOut1.format(date1)
        }
    }
}


fun setMessage(it: Throwable, con: Context): String {
    return when (it) {
        is UnknownHostException -> con.resources.getString(R.string.no_internet)
        is HttpException -> {
            var text = "some error"
            it.response()?.also { resp -> text = resp.message() }
            return text
        }
        else -> it.message.toString()
    }
}

fun View.clickBtn(scope: ScopeProvider, success: (View) -> Unit) {
    this.clicks()
        .throttleFirst(1, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(scope)
        .subscribe { success.invoke(this@clickBtn) }
}
