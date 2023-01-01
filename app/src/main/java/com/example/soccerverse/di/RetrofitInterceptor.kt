package com.example.soccerverse.di

import com.example.soccerverse.BuildConfig
import com.example.soccerverse.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request  = chain.request()
            .newBuilder()
            .addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
            .addHeader("X-RapidAPI-Host", Constants.API_HOST)
            .build()
        return chain.proceed(request)
    }
}