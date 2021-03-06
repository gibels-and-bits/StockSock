package com.yum.stocksock.stockList.mvi.action

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockList.mvi.StockListViewState

class StartLoading : Action<StockListViewState> {
    override fun perform(currentState: StockListViewState): StockListViewState = currentState.copy(
        loading = true,
        allStocks = emptyList(),
        errorMessage = null
    )
}
