package com.yum.stocksock.di

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.yum.stocksock.network.api.StockTickerService
import okhttp3.OkHttpClient
import toothpick.InjectConstructor
import javax.inject.Provider

@InjectConstructor
class StockTickerServiceProvider(
    lifecycle: Lifecycle,
    client: OkHttpClient
) : Provider<StockTickerService> {

    private val scarlet = Scarlet.Builder()
        .webSocketFactory(client.newWebSocketFactory("wss://interviews.yum.dev/ws/stocks"))
        .lifecycle(lifecycle)
        .addMessageAdapterFactory(GsonMessageAdapter.Factory())
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .build()

    override fun get(): StockTickerService = scarlet.create()

}