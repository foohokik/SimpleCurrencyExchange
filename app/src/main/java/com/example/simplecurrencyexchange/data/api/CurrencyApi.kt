package com.example.simplecurrencyexchange.data.api

import com.example.appnews.core.network.NetworkResult
import com.example.simplecurrencyexchange.data.model.CurrencyResponse
import com.example.simplecurrencyexchange.domain.model.Currency
import retrofit2.http.GET


interface CurrencyApi {

    @GET("/daily_json.js")
    suspend fun getCurrencies(): NetworkResult<CurrencyResponse>
}