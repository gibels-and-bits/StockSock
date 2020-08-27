package com.yum.stocksock.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Provider

class OkHttpProvider : Provider<OkHttpClient> {
    companion object {
        val LOG_LEVEL = HttpLoggingInterceptor.Level.HEADERS
    }

    override fun get() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(LOG_LEVEL))
        .build()
}