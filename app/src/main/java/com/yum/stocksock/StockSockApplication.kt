package com.yum.stocksock

import android.app.Application
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.yum.stocksock.di.OkHttpProvider
import com.yum.stocksock.di.StockDetailServiceProvider
import com.yum.stocksock.di.StockTickerServiceProvider
import com.yum.stocksock.network.api.StockDetailService
import com.yum.stocksock.network.api.StockTickerService
import okhttp3.OkHttpClient
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class StockSockApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val toothpickModule = module {
            bind<OkHttpClient>().toProviderInstance(OkHttpProvider()).providesSingleton()
            bind<Lifecycle>().toInstance { AndroidLifecycle.ofApplicationForeground(this@StockSockApplication) }
            bind<StockTickerService>().toProvider(StockTickerServiceProvider::class).providesSingleton()
            bind<StockDetailService>().toProvider(StockDetailServiceProvider::class).providesSingleton()
        }

        KTP.openRootScope().installModules(toothpickModule)
    }
}