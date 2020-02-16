package com.test.di

import android.content.Context
import com.test.data.PreferencesManager
import com.test.utils.*
import okhttp3.*
import org.json.JSONObject

class TokenAuthenticator(
    private val prefManager: PreferencesManager,
    private val context: Context,
    private val okHttpClientBuilder: OkHttpClient.Builder
) : Authenticator {

    override fun authenticate(route: Route?, response: Response?): Request? {

        if (response?.code() == 401 && response.message() == "Unauthorized") {

            val pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            val password = pref.getString(PASSWORD, "")
            val login = pref.getString(LOGIN, "")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", password)
                .addFormDataPart("username", login)
                .build()

            val request = Request.Builder()
                .url(BASE_URL + "api/login/")
                .post(requestBody)
                .build()

            val okHttpClient = okHttpClientBuilder.build()
            val resp = okHttpClient.newCall(request).execute()
            val body = resp.body()?.string()
            val jsonObj = JSONObject(body)
            val token: String = jsonObj.get(TOKEN).toString()

            pref.edit().putString(TOKEN, token).apply()

            return response.request().newBuilder()
                .header(TOKEN_NAME, TOKEN_PREFIX + token)
                .build()
        } else {
            return null
        }
    }
}