package com.yum.stocksock.mvi

/**
 * Actions simply turn a previous ViewState into a new one.  Any data an action may use
 * to mutate that ViewState is stored inside the Action data class itself.
 */
interface Action<VIEWSTATE> {
    fun perform(currentState: VIEWSTATE): VIEWSTATE
}
