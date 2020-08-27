package com.yum.stocksock.stockList.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFadeThrough
import com.jakewharton.rxrelay2.PublishRelay
import com.yum.stocksock.databinding.TemplateStockListBinding
import com.yum.stocksock.stockList.mvi.StockListViewState
import com.yum.stocksock.stockList.mvi.event.StockItemClick
import com.yum.stocksock.stockList.mvi.event.StockListEvent
import io.reactivex.Observable

class StockListTemplate : ConstraintLayout {

    private val uiEventEmitter = PublishRelay.create<StockListEvent>()
    private val binding by lazy { TemplateStockListBinding.inflate(LayoutInflater.from(context), this) }

    private val recyclerView: RecyclerView
    private val listAdapter: StockListAdapter

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        listAdapter = StockListAdapter { uiEventEmitter.accept(StockItemClick(it)) }
        recyclerView = binding.stockListRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    fun render(viewState: StockListViewState) {

        if (!viewState.errorMessage.isNullOrBlank()) {
            binding.stockListErrorTv.text = viewState.errorMessage
            binding.stockListErrorTv.visibility = VISIBLE
        } else {
            binding.stockListErrorTv.visibility = GONE
        }

        if (viewState.allStocks.isNotEmpty()) {
            if (recyclerView.visibility != VISIBLE) {
                transitionToLoadedList()
            }
        } else {
            recyclerView.visibility = View.GONE
        }

        listAdapter.updateList(viewState.filteredStocks)
        binding.companyFilter.setSelectedCategories(viewState.typesToShowFilter)
        binding.companyFilter.setCategories(viewState.companyTypes)
    }

    private fun transitionToLoadedList() {
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1500
        }
        TransitionManager.beginDelayedTransition(this, fadeThrough)
        binding.loadingAnimation.cancelAnimation()
        binding.lottieLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun observe(): Observable<StockListEvent> = uiEventEmitter.hide().mergeWith(binding.companyFilter.observe())
}