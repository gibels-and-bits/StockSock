package com.yum.stocksock.di

import com.yum.stocksock.network.api.StockDetailService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import toothpick.InjectConstructor
import javax.inject.Provider

@InjectConstructor
class StockDetailServiceProvider(
    client: OkHttpClient
) : Provider<StockDetailService> {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://interviews.yum.dev")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun get() = retrofit.create<StockDetailService>()

}