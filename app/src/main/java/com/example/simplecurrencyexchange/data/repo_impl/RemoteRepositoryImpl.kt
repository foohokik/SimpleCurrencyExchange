package com.example.simplecurrencyexchange.data.repo_impl

import com.example.simplecurrencyexchange.core.network.NetworkResult
import com.example.simplecurrencyexchange.data.api.CurrencyApi
import com.example.simplecurrencyexchange.data.model.toNetworkResultCurrency
import com.example.simplecurrencyexchange.domain.RemoteRepository
import com.example.simplecurrencyexchange.domain.model.Currency
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val api: CurrencyApi):RemoteRepository {

    override suspend fun getCurrencies(): NetworkResult<Currency> = api.getCurrencies().toNetworkResultCurrency()

}