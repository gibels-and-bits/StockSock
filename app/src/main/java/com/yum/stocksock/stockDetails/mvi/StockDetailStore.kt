package com.yum.stocksock.stockDetails.mvi

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.mvi.OneTimeEventEmitter
import com.yum.stocksock.mvi.Store
import com.yum.stocksock.stockDetails.mvi.event.DetailScreenLoaded
import com.yum.stocksock.stockDetails.mvi.usecase.loadDetails
import io.reactivex.Observable

class StockDetailStore : Store<DetailScreenLoaded, StockDetailsViewState>() {

    override fun Observable<DetailScreenLoaded>.eventToActions(eventEmitter: OneTimeEventEmitter):
        Observable<out Action<StockDetailsViewState>> = loadDetails()

    override fun makeDefaultViewState() = StockDetailsViewState()
}