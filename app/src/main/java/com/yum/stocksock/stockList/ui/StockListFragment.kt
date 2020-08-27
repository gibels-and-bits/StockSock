package com.yum.stocksock.stockList.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yum.stocksock.R
import com.yum.stocksock.databinding.FragmentStockListBinding
import com.yum.stocksock.mvi.NavigateToStockDetails
import com.yum.stocksock.mvi.OneTimeEvent
import com.yum.stocksock.network.model.StockIdentifier
import com.yum.stocksock.stockList.mvi.StockListStore
import com.yum.stocksock.stockList.mvi.event.SearchTextEntered
import com.yum.stocksock.stockList.mvi.event.StockListScreenLoad
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

/** Most of the bloat here is due to how Android SDK handles the searchView.  We have to
 * maintain it's contents separately so that it persists on rotation.  Additionally, we
 * need to watch for backpress events to close the search view instead of closing the
 * Fragment on backpress.
 */
class StockListFragment : Fragment() {
    private val store: StockListStore by viewModels()
    private val disposables = CompositeDisposable()

    private var _binding: FragmentStockListBinding? = null
    private lateinit var searchView: SearchView
    private var searchViewText = ""

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStockListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                } else {
                    handleOnBackPressed()
                }
            }
        })

        if (savedInstanceState != null) {
            searchViewText = savedInstanceState.getString("search", "")
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("search", searchViewText)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.search_icon)
        searchView = search.actionView as SearchView

        searchView.apply {
            queryHint = "Search"

            if (searchViewText.isNotBlank()) {
                onActionViewExpanded()
                setQuery(searchViewText, true)
                clearFocus()
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        store.sendEvent(SearchTextEntered(newText))
                        searchViewText = it
                    }

                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val template = binding.stockListTemplate

        /* The main job of the Fragment or Activity in this pattern is to listen to the viewModel.
           We subscribe to event streams FROM the viewModel as well as event streams TO the viewModel
           in the form of Observable<EVENT> from the templates.
         */

        disposables.add(
            store.viewState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(template::render) { e -> Log.e(LOG_TAG, e.message.orEmpty()) }
        )

        disposables.add(
            store.oneTimeEvents
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::handleOneTImeEvents) { e -> Log.e(LOG_TAG, e.message.orEmpty()) }
        )

        disposables.add(
            template.observe()
                .subscribe(store::sendEvent) { e -> Log.e(LOG_TAG, e.message.orEmpty()) }
        )

        store.sendEvent(StockListScreenLoad)
    }

    override fun onDestroyView() {
        _binding = null
        disposables.clear()
        super.onDestroyView()
    }

    private fun handleOneTImeEvents(event: OneTimeEvent) {
        when (event) {
            is NavigateToStockDetails -> navigateToDetails(event.id)
        }
    }

    private fun navigateToDetails(stockId: StockIdentifier) {
        val action = StockListFragmentDirections.actionStockListFragmentToStockDetailsFragment(stockId)
        findNavController().navigate(action)
    }

    companion object {
        private const val LOG_TAG = "StockSock MVI Error"
    }
}