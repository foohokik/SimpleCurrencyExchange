package com.example.simplecurrencyexchange.domain

import com.example.appnews.core.network.NetworkResult
import com.example.simplecurrencyexchange.domain.model.Currency

interface RemoteRepository {

    suspend fun getCurrencies():NetworkResult<Currency>

}