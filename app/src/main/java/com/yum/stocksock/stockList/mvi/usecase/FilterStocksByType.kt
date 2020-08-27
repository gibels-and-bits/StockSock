package com.yum.stocksock.stockList.mvi.usecase

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockList.mvi.StockListViewState
import com.yum.stocksock.stockList.mvi.action.ApplyStockFilters
import com.yum.stocksock.stockList.mvi.event.FilterTypeChanged
import io.reactivex.Observable

internal fun Observable<FilterTypeChanged>.filterStockList() : Observable<out Action<StockListViewState>> {
    return map { ApplyStockFilters(typeFilters = it.filters) }
}