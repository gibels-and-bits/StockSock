package com.yum.stocksock.stockDetails.ui.model

import com.yum.stocksock.network.model.StockIdentifier

data class StockDetailDisplay (
    val id: StockIdentifier,
    val name: String,
    val price: String,
    val companyType: List<String>,
    val allTimeHigh: String,
    val address: String,
    val imageUrl: String,
    val website: String
)