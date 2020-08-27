package com.yum.stocksock.stockList.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yum.stocksock.databinding.ListItemStockBinding
import com.yum.stocksock.network.model.StockIdentifier
import com.yum.stocksock.stockList.ui.model.Direction
import com.yum.stocksock.stockList.ui.model.StockListItem

class StockListAdapter(
    private val clickHandler: (StockIdentifier) -> Unit
) : RecyclerView.Adapter<StockListAdapter.StockListViewHolder>() {

    private var dataSet = emptyList<StockListItem>()

    class StockListViewHolder(
        private val binding: ListItemStockBinding,
        private val clickHandler: (StockIdentifier) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockListItem) {
            binding.stockName.text = item.name
            binding.stockSymbol.text = item.id
            binding.currentPrice.text = item.price
            binding.priceDeltaAbs.text = item.delta.magnitudeDiffDisplay
            binding.priceDeltaAbs.setTextColor(colorForDirection(item.delta.direction))
            binding.root.setOnClickListener { clickHandler(item.id) }
        }

        private fun colorForDirection(direction: Direction) = when (direction) {
            Direction.UP -> Color.GREEN
            Direction.DOWN -> Color.RED
            Direction.NONE -> Color.WHITE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockListViewHolder {
        val binding = ListItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockListViewHolder(binding, clickHandler)
    }

    override fun onBindViewHolder(holder: StockListViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun updateList(stocks: List<StockListItem>) {
        dataSet = stocks
        notifyDataSetChanged()
    }
}
