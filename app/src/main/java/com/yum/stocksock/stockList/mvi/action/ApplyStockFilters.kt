package com.yum.stocksock.stockList.mvi.action

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.stockList.filterByText
import com.yum.stocksock.stockList.filterByType
import com.yum.stocksock.stockList.mvi.StockListViewState

class ApplyStockFilters(
    val textFilter: String? = null,
    val typeFilters: Set<String>? = null
) : Action<StockListViewState> {

    override fun perform(currentState: StockListViewState): StockListViewState {
        val textFilter = textFilter ?: currentState.stockTextFilter
        val typeFilter = typeFilters ?: currentState.typesToShowFilter

        return currentState.copy(
            filteredStocks = currentState.allStocks.filterByText(textFilter).filterByType(typeFilter),
            stockTextFilter = textFilter,
            typesToShowFilter = typeFilter
        )
    }
}