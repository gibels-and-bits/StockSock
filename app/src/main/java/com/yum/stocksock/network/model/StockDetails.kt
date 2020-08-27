package com.yum.stocksock.network.model

import java.math.BigDecimal

data class StockDetails(
    val id: StockIdentifier?,
    val name: String?,
    val price: BigDecimal?,
    val companyType: List<String>?,
    val allTimeHigh: BigDecimal?,
    val address: String?,
    val imageUrl: String?,
    val website: String?
)