package com.yum.stocksock.stockList.mvi.action

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockList.mvi.StockListViewState

class NoEffectAction : Action<StockListViewState> {

    // Side effects don't affect state
    override fun perform(currentState: StockListViewState) = currentState
}