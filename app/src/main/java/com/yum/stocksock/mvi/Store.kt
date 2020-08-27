package com.yum.stocksock.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// Leverage the Kotlin type system when possible to avoid the ambiguity of passing around generic types like Relays
typealias OneTimeEventEmitter = PublishRelay<OneTimeEvent>


/**
 * The ViewModel is parameterized with the Event and ViewState.  This allows us to basically use the
 * same ViewModel for every feature we have since the only real changes are those types.  In a more complex
 * app, you'd want to also parameterize for the OneTimeEvent and make that an interface.
 *
 * Notice, the Store delegates to it's feature-specific children for only two jobs:
 *
 * 1. Map a screen specific event to an Action - This can't be done polimorphically since I'm using type
 * based extension functions for the Use Cases.  That is to say, you can't have EVENT.doAThing() in the general
 * sense, those extensions rely on the concrete type and therefore must be provided by the ViewModel subclasses.
 *
 * 2. The default ViewState.  The scan operator in this abstract class depends on seeding with a default viewstate.
 * Since here we are dealing with the VIEWSTATE as an abstract parameterization, we can't directly instantiate and
 * so have to delegate to our children to do so.
 *
 * Connectable Observable:
 * The main observable stream handling all the conversions from Event->UseCase->Action is initialized at construction
 * and is multicast.  We don't want direction subscriptions to this stream since those subscriptions are disposed on the
 * Fragment and Activity lifecycle boundaries.  If that happened, the stream would effectively be dead after the first
 * device rotation since it would have received a terminal event but had not been recreated.
 *
 * Instead, multicasting [replay(1)] gives us a ConnectableObservable that functions as a HOT stream - continually
 * emitting even when subscribers.  The effect is that this internal stream continues to emit and stays alive as
 * long as the ViewModel itself.
 *
 */
abstract class Store<EVENT, VIEWSTATE> : ViewModel() {

    private val eventEmitter = PublishRelay.create<EVENT>()
    private val disposables = CompositeDisposable()
    private val oneTimeEventEmitter = OneTimeEventEmitter.create<OneTimeEvent>()

    protected abstract fun Observable<EVENT>.eventToActions(eventEmitter: OneTimeEventEmitter): Observable<out Action<VIEWSTATE>>
    protected abstract fun makeDefaultViewState(): VIEWSTATE

    val viewState: Observable<VIEWSTATE>
    val oneTimeEvents: Observable<OneTimeEvent>

    init {
        val observable = eventEmitter
            .observeOn(Schedulers.io())
            .doOnNext { event -> Log.d(LOG_TAG, "----- event $event") }
            .eventToActions(oneTimeEventEmitter)
            .doOnNext { action -> Log.d(LOG_TAG, "----- action $action") }
            .actionToViewState()
            .doOnNext { state -> Log.d(LOG_TAG, "----- view state $state") }
            .replay(1)

        observable.connect { disposables.add(it) }
        viewState = observable.subscribeOn(Schedulers.io())
        oneTimeEvents = oneTimeEventEmitter.hide()
    }

    override fun onCleared() {
        disposables.dispose()
    }

    private fun Observable<out Action<VIEWSTATE>>.actionToViewState(): Observable<VIEWSTATE> {
        return scan(makeDefaultViewState()) { currentState, action ->
            action.perform(currentState)
        }.skip(1).distinctUntilChanged()
    }

    fun sendEvent(event: EVENT) {
        eventEmitter.accept(event)
    }

    companion object {
        private const val LOG_TAG = "StockSock MVI"
    }
}