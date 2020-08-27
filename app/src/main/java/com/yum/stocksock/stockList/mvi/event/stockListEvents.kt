package com.yum.stocksock.stockList.mvi.event

sealed class StockListEvent

object StockListScreenLoad : StockListEvent()

data class StockItemClick(val id: String) : StockListEvent()

data class SearchTextEntered(val value: String) : StockListEvent()

data class FilterTypeChanged(val filters: Set<String>) : StockListEvent()
