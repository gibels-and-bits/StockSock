package com.yum.stocksock.mvi.converter

import com.yum.stocksock.network.model.Stock
import com.yum.stocksock.stockList.ui.model.StockDelta
import com.yum.stocksock.stockList.ui.model.StockListItem
import toothpick.InjectConstructor
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@InjectConstructor
class StockConverter : ApiModelConverter<Stock, StockListItem> {

    override fun convertFrom(model: Stock?): StockListItem? {
        if (model == null) {
            return null
        }
        if (model.id == null) {
            return null
        }

        return StockListItem(
            id = model.id,
            name = model.name ?: "",
            price = model.price?.toCurrencyString() ?: "",
            delta = StockDelta(absValue = model.price ?: BigDecimal(0.0)),
            types = model.companyType ?: listOf()
        )
    }

    private fun BigDecimal.toCurrencyString(): String {
        val usdCostFormat = NumberFormat.getCurrencyInstance(Locale.US)
        usdCostFormat.minimumFractionDigits = 1
        usdCostFormat.maximumFractionDigits = 2
        return usdCostFormat.format(toDouble())
    }
}