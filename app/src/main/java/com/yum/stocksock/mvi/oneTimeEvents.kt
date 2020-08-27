package com.yum.stocksock.mvi

import com.yum.stocksock.network.model.StockIdentifier

/**
 * Sometimes we don't want to replay state.  Things like navigation, dialogs, snackbars, toasts... once they
 * happen we don't want them to repeat if you were to rotate your device for instance.  To get around this
 * limitation, we use a separate channel for "One time events".
 */
sealed class OneTimeEvent

data class NavigateToStockDetails(val id: StockIdentifier) : OneTimeEvent()
