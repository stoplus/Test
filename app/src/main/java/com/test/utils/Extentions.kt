package com.test.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.jakewharton.rxbinding3.view.clicks
import com.test.R
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.autoDisposable
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
            var text = "error"
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


fun EditText.afterTextChanges(onAfterTextChanged: (String, EditText) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            onAfterTextChanged.invoke(s.toString(), this@afterTextChanges)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val n = cm.activeNetwork
    if (n != null) {
        val nc = cm.getNetworkCapabilities(n)
        return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI
        )
    }
    return false
}
