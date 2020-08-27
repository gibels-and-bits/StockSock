package com.yum.stocksock.stockDetails.mvi.action

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockDetails.mvi.StockDetailsViewState
import com.yum.stocksock.stockDetails.ui.model.StockDetailDisplay

class DisplayStockDetails(private val details: StockDetailDisplay) : Action<StockDetailsViewState> {

    override fun perform(currentState: StockDetailsViewState): StockDetailsViewState = currentState.copy(
        loading = false,
        details = details
    )
}