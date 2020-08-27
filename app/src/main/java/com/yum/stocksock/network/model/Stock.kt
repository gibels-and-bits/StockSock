package com.yum.stocksock.network.model

import java.math.BigDecimal

typealias StockIdentifier = String

data class Stock(
    val id: StockIdentifier?,
    val name: String?,
    val price: BigDecimal?,
    val companyType: List<String>?
)
