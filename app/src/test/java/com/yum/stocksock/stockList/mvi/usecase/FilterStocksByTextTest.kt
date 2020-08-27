package com.yum.stocksock.stockList.mvi.usecase

import com.yum.stocksock.stockList.mvi.action.ApplyStockFilters
import com.yum.stocksock.stockList.mvi.event.SearchTextEntered
import io.reactivex.Observable
import org.junit.Test

class FilterStocksByTextTest {

    private val emptyStringSource = SearchTextEntered("")
    private val nonEmptyStringSource = SearchTextEntered("YUM")

    @Test
    fun filterStockList_onReceivesEmptyString_returnsActionWithEmptyStringAndNullTypeFilter(){
        val actionStream = Observable.just(emptyStringSource).filterStockList()
        actionStream.ofType(ApplyStockFilters::class.java).test().assertValue {
            it.textFilter == "" && it.typeFilters == null
        }
    }

    @Test
    fun filterStockList_onReceivesNonEmptyString_returnsActionWithSameStringAndNullTypeFilter(){
        val actionStream = Observable.just(nonEmptyStringSource).filterStockList()
        actionStream.ofType(ApplyStockFilters::class.java).test().assertValue {
            it.textFilter == nonEmptyStringSource.value && it.typeFilters == null
        }
    }
}