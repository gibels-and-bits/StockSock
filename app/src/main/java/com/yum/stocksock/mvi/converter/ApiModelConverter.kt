package com.yum.stocksock.mvi.converter

/**
 * We don't want to be dealing with network models in business logic.  These converters provide an easy interface
 * to convert from network to domain layer.  In a more complex app, this converter may provide a domain->netowrk
 * mapping as well.
 */
interface ApiModelConverter<FROM, TO> {
    fun convertFrom(model: FROM?): TO?
}