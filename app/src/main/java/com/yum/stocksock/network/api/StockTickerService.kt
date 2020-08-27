package com.yum.stocksock.network.api

import com.tinder.scarlet.ws.Receive
import com.yum.stocksock.network.model.Stock
import io.reactivex.Flowable

/** Scarlet Interface providing for a Retrofit-like client builder.  Notice the Flowable
 * returned to handle backpressure if the websocket has a high SEND rate.
 */
interface StockTickerService {

    @Receive
    fun observeStockInfo(): Flowable<List<Stock>>
}