package com.yum.stocksock.network.api

import com.yum.stocksock.network.model.StockDetails
import com.yum.stocksock.network.model.StockIdentifier
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Standard Retrofit API for Stock Details
 */
interface StockDetailService{

    @GET("/api/stocks/{id}")
    fun getDetailsForId(@Path("id") identifier: StockIdentifier) : Observable<StockDetails>
}