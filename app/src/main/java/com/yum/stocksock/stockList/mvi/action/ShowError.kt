package com.yum.stocksock.stockList.mvi.action

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockList.mvi.StockListViewState

class ShowError(private val errorMessage: String) : Action<StockListViewState> {
    override fun perform(currentState: StockListViewState): StockListViewState = currentState.copy(
        errorMessage = errorMessage,
        allStocks = emptyList(),
        loading = false
    )
}
