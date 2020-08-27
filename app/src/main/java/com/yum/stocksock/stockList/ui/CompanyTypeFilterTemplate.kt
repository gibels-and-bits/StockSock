package com.yum.stocksock.stockList.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxrelay2.PublishRelay
import com.yum.stocksock.R
import com.yum.stocksock.stockList.mvi.event.FilterTypeChanged
import com.yum.stocksock.stockList.mvi.event.StockListEvent

class CompanyTypeFilterTemplate : ChipGroup {
    private val uiEventEmitter = PublishRelay.create<StockListEvent>()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.template_company_filter, this)
    }

    private val selectedCatSet = mutableSetOf<String>()
    private var hasBeenSetup: Boolean = false

    fun setCategories(companyTypes: Set<String>) {
        if (companyTypes.isNotEmpty() && !hasBeenSetup) {
            removeAllViews()
            companyTypes.forEach { companyType ->
                val chip = Chip(context, null, R.attr.CustomChipChoiceStyle).apply {
                    text = companyType
                    isChecked = companyType in selectedCatSet
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            selectedCatSet.add(companyType)
                        } else {
                            selectedCatSet.remove(companyType)
                        }
                        uiEventEmitter.accept(FilterTypeChanged(selectedCatSet))
                    }
                }
                addView(chip)
            }
            hasBeenSetup = true
        }
    }

    fun setSelectedCategories(selected: Set<String>){
        if(selectedCatSet.isEmpty() && selected.isNotEmpty()) {
            selectedCatSet += selected
        }
    }

    fun observe() = uiEventEmitter.hide()
}