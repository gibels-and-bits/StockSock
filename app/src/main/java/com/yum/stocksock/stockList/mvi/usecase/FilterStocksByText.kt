package com.yum.stocksock.stockList.mvi.usecase

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockList.mvi.StockListViewState
import com.yum.stocksock.stockList.mvi.action.ApplyStockFilters
import com.yum.stocksock.stockList.mvi.event.SearchTextEntered
import io.reactivex.Observable

internal fun Observable<SearchTextEntered>.filterStockList() : Observable<out Action<StockListViewState>> {
    return map { ApplyStockFilters(textFilter = it.value) }
}