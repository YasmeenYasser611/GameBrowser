package com.example.gamebrowser.data.remote

import android.util.Log
import com.example.gamebrowser.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        Log.d("API_KEY_CHECK", "RAWG key = ${BuildConfig.RAWG_API_KEY}")

        val originalUrl = chain.request().url
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.RAWG_API_KEY)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
