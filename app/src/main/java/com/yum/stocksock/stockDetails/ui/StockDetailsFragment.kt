package com.yum.stocksock.stockDetails.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.yum.stocksock.databinding.FragmentStockDetailsBinding
import com.yum.stocksock.stockDetails.mvi.StockDetailStore
import com.yum.stocksock.stockDetails.mvi.event.DetailScreenLoaded
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class StockDetailsFragment : Fragment() {
    private val store: StockDetailStore by viewModels()
    val args: StockDetailsFragmentArgs by navArgs()

    private val disposables = CompositeDisposable()

    private var _binding: FragmentStockDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStockDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val template = binding.stockDetailTemplate

        disposables.add(
            store.viewState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(template::render) { e -> Log.e(LOG_TAG, e.message.orEmpty()) }
        )

        disposables.add(
            store.oneTimeEvents
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )

        store.sendEvent(DetailScreenLoaded(args.stockId))
    }

    override fun onDestroyView() {
        _binding = null
        disposables.clear()
        super.onDestroyView()
    }

    companion object {
        private const val LOG_TAG = "StockSock MVI Error"
    }
}