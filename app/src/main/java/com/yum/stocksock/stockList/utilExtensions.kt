package com.yum.stocksock.stockList

import com.yum.stocksock.stockList.ui.model.StockListItem

internal fun List<StockListItem>.filterByText(prefix: String): List<StockListItem> =
    filter { it.name.toUpperCase().startsWith(prefix.toUpperCase()) }

internal fun List<StockListItem>.filterByType(filters: Set<String>): List<StockListItem> =
    filter { item ->
        filters.isEmpty() || filters.any {
            item.types.contains(it)
        }
    }

