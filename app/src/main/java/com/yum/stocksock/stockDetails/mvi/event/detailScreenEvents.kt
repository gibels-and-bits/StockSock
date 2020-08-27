package com.yum.stocksock.stockDetails.mvi.event

import com.yum.stocksock.network.model.StockIdentifier

// Just one event for this screen so no need for a sealed class
data class DetailScreenLoaded(val displayedDetailId: StockIdentifier)