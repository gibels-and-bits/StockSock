package com.yum.stocksock.stockList.mvi.usecase

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockList.mvi.action.RefreshStocks
import com.yum.stocksock.stockList.mvi.action.ShowError
import com.yum.stocksock.stockList.mvi.action.StartLoading
import com.yum.stocksock.mvi.converter.StockConverter
import com.yum.stocksock.network.api.StockTickerService
import com.yum.stocksock.stockList.mvi.StockListViewState
import com.yum.stocksock.stockList.mvi.event.StockListScreenLoad
import io.reactivex.Observable
import toothpick.ktp.KTP
import toothpick.ktp.extension.getInstance

internal fun Observable<StockListScreenLoad>.loadScreen(): Observable<out Action<StockListViewState>> {

    val rootScope = KTP.openRootScope()
    val stockService: StockTickerService = rootScope.getInstance()
    val apiConverter: StockConverter = rootScope.getInstance()

    return switchMap {
        stockService.observeStockInfo()
            .map { apiStockList -> apiStockList.mapNotNull { apiConverter.convertFrom(it) } }
            .map<Action<StockListViewState>> { stockListItems -> RefreshStocks(stockListItems) }
            .toObservable()
            .startWith(StartLoading())
            .onErrorReturnItem(ShowError("Cannot load the stocks"))
    }
}
