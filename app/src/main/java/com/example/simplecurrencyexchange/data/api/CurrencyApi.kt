package com.example.simplecurrencyexchange.data.api

import com.example.simplecurrencyexchange.core.network.NetworkResult
import com.example.simplecurrencyexchange.data.model.CurrencyResponse
import retrofit2.http.GET


interface CurrencyApi {

    @GET("/daily_json.js")
    suspend fun getCurrencies(): NetworkResult<CurrencyResponse>
}