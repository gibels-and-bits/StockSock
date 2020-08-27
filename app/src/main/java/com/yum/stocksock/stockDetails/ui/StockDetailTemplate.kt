package com.yum.stocksock.stockDetails.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ScrollView
import com.bumptech.glide.Glide
import com.yum.stocksock.databinding.TemplateStockDetailsBinding
import com.yum.stocksock.stockDetails.mvi.StockDetailsViewState

class StockDetailTemplate : ScrollView {

    private val binding by lazy { TemplateStockDetailsBinding.inflate(LayoutInflater.from(context), this) }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun render(viewState: StockDetailsViewState) {
        viewState.details?.let {
            binding.stockName.text = it.name
            binding.stockId.text = it.id
            binding.address.text = it.address
            binding.website.text = it.website

            if (it.imageUrl.isNotBlank()) {
                Glide
                    .with(context)
                    .load(it.imageUrl)
                    .into(binding.companyLogo);
            }
        }
    }
}