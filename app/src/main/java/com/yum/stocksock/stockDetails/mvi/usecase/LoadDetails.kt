package com.yum.stocksock.stockDetails.mvi.usecase

import com.yum.stocksock.mvi.converter.StockDetailConverter
import com.yum.stocksock.mvi.Action
import com.yum.stocksock.network.api.StockDetailService
import com.yum.stocksock.stockDetails.mvi.StockDetailsViewState
import com.yum.stocksock.stockDetails.mvi.event.DetailScreenLoaded
import com.yum.stocksock.stockDetails.mvi.action.DisplayStockDetails
import io.reactivex.Observable
import toothpick.ktp.KTP
import toothpick.ktp.extension.getInstance

internal fun Observable<DetailScreenLoaded>.loadDetails(): Observable<out Action<StockDetailsViewState>> {
    val rootScope = KTP.openRootScope()
    val stockDetailService: StockDetailService = rootScope.getInstance()
    val apiConverter: StockDetailConverter = rootScope.getInstance()

    return switchMap { stockDetailService.getDetailsForId(it.displayedDetailId) }
        .map { stockDetailApiModel -> apiConverter.convertFrom(stockDetailApiModel) }
        .map<Action<StockDetailsViewState>> { details -> DisplayStockDetails(details) }
}