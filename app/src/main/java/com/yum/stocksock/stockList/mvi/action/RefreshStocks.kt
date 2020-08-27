package com.yum.stocksock.stockList.mvi.action

import com.yum.stocksock.mvi.Action
import com.yum.stocksock.network.model.StockIdentifier
import com.yum.stocksock.stockList.filterByText
import com.yum.stocksock.stockList.filterByType
import com.yum.stocksock.stockList.mvi.StockListViewState
import com.yum.stocksock.stockList.ui.model.Direction
import com.yum.stocksock.stockList.ui.model.StockDelta
import com.yum.stocksock.stockList.ui.model.StockListItem
import java.math.BigDecimal

/**
 * This Action is responsible for computing the difference since the last update.  We must
 * do that work here instead of a Usecase since it relies on the previous state.
 */
class RefreshStocks(private val stocks: List<StockListItem>) : Action<StockListViewState> {
    override fun perform(currentState: StockListViewState): StockListViewState {

        val deltaMap = computeDiff(currentState.deltaMap, stocks)
        val companyTypes = stocks.map { it.types }.flatten().toSet()
        val allStocks = stocks.augmentWithDeltas(deltaMap)

        return currentState.copy(
            allStocks = allStocks,
            filteredStocks = allStocks
                .filterByText(currentState.stockTextFilter)
                .filterByType(currentState.typesToShowFilter),
            loading = false,
            errorMessage = null,
            deltaMap = deltaMap,
            companyTypes = companyTypes
        )
    }

    /**
     * Each state update maintains a mapping of Stock "Deltas".  This gives me sufficient
     * information to compute the stock vector (both direction and magnitude) given
     * only the previous mapping and the incoming stock values
     */
    private fun computeDiff(
        previousDeltas: Map<StockIdentifier, StockDelta>,
        updatedValues: List<StockListItem>
    ): Map<StockIdentifier, StockDelta> =
        updatedValues.fold(mutableMapOf()) { newMap, curr ->
            newMap.apply {
                put(curr.id, computeNewDelta(previousDeltas[curr.id], curr.delta))
            }
        }

    /**
     * Given the current Delta and the new Delta update, compute a new Delta which
     * defines how the stock has changed since the last update.  Notice the models
     * here output values that are ready to be consumed by UI without further processing.
     */
    private fun computeNewDelta(old: StockDelta?, updated: StockDelta): StockDelta {
        if (old == null) return updated

        val oldValue = old.absValue.setScale(2, BigDecimal.ROUND_HALF_EVEN)
        val updatedValue = updated.absValue.setScale(2, BigDecimal.ROUND_HALF_EVEN)
        val difference = updatedValue.minus(oldValue).abs().toString()

        val directionChange = when {
            updatedValue > oldValue -> Direction.UP
            updatedValue < oldValue -> Direction.DOWN
            else -> Direction.NONE
        }

        return updated.copy(
            magnitudeDiffDisplay = when (directionChange) {
                Direction.UP -> "(+$difference)"
                Direction.DOWN -> ("(-$difference)")
                Direction.NONE -> "--"
            },
            direction = directionChange
        )
    }

    /**
     * Add in the new delta maps so that we can do the next comparison when new stocks come in.
     */
    private fun List<StockListItem>.augmentWithDeltas(deltas: Map<StockIdentifier, StockDelta>) =
        this.map { item ->
            val newDelta = deltas[item.id]

            newDelta?.let {
                item.copy(
                    delta = newDelta
                )
            } ?: item
        }
}
