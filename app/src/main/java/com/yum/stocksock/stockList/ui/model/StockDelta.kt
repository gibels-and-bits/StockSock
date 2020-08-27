package com.yum.stocksock.stockList.ui.model

import java.math.BigDecimal

enum class Direction { UP, DOWN, NONE}

const val emptyValue = "--"

data class StockDelta(
    val magnitudeDiffDisplay: String = emptyValue,
    val direction: Direction = Direction.NONE,
    val absValue: BigDecimal = BigDecimal(0.0)
)