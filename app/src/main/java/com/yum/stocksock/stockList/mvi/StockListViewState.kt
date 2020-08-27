package com.yum.stocksock.stockList.mvi

import com.yum.stocksock.network.model.StockIdentifier
import com.yum.stocksock.stockList.ui.model.StockDelta
import com.yum.stocksock.stockList.ui.model.StockListItem

data class StockListViewState(
    val allStocks: List<StockListItem> = emptyList(),
    val filteredStocks: List<StockListItem> = emptyList(),
    val deltaMap: Map<StockIdentifier, StockDelta> = mapOf(),
    val errorMessage: String? = null,
    val loading: Boolean = false,
    val stockTextFilter: String = "",
    val typesToShowFilter: Set<String> = setOf(),
    val companyTypes: Set<String> = setOf()
)