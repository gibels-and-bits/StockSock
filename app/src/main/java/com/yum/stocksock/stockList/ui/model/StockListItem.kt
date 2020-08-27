package com.yum.stocksock.stockList.ui.model

import com.yum.stocksock.network.model.StockIdentifier

data class StockListItem(
    val id: StockIdentifier,
    val name: String,
    val price: String,
    val delta: StockDelta,
    val types: List<String>
)