package com.yum.stocksock.stockList.mvi.usecase

import com.yum.stocksock.mvi.NavigateToStockDetails
import com.yum.stocksock.mvi.OneTimeEventEmitter
import com.yum.stocksock.stockList.mvi.action.NoEffectAction
import com.yum.stocksock.stockList.mvi.event.StockItemClick
import io.reactivex.Observable

internal fun Observable<StockItemClick>.loadDetails(eventPublisher: OneTimeEventEmitter) =
    doOnNext { eventPublisher.accept(NavigateToStockDetails(it.id)) }
        .map { NoEffectAction() }