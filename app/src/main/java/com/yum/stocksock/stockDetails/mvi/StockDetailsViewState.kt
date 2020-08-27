package com.yum.stocksock.stockDetails.mvi

import com.yum.stocksock.stockDetails.ui.model.StockDetailDisplay

data class StockDetailsViewState(
    val details: StockDetailDisplay? = null,
    val loading: Boolean = false,
    val error: String? = null
)