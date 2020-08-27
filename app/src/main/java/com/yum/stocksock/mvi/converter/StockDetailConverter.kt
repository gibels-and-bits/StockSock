package com.yum.stocksock.mvi.converter

import com.yum.stocksock.network.model.StockDetails
import com.yum.stocksock.stockDetails.ui.model.StockDetailDisplay
import toothpick.InjectConstructor
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@InjectConstructor
class StockDetailConverter : ApiModelConverter<StockDetails, StockDetailDisplay> {

    override fun convertFrom(model: StockDetails?): StockDetailDisplay? {
        if (model == null) {
            return null
        }
        if (model.id == null) {
            return null
        }

        return StockDetailDisplay(
            id = model.id,
            name = model.name ?: "",
            price = model.price?.toCurrencyString() ?: "",
            companyType = listOf(),
            allTimeHigh = model.allTimeHigh?.toCurrencyString() ?: "",
            address = model.address ?: "",
            imageUrl = model.imageUrl ?: "",
            website = model.website ?: ""
        )
    }

    private fun BigDecimal.toCurrencyString(): String {
        val usdCostFormat = NumberFormat.getCurrencyInstance(Locale.US)
        usdCostFormat.minimumFractionDigits = 1
        usdCostFormat.maximumFractionDigits = 2
        return usdCostFormat.format(toDouble())
    }
}