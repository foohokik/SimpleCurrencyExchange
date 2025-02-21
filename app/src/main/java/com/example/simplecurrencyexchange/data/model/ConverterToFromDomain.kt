package com.example.simplecurrencyexchange.data.model

import com.example.simplecurrencyexchange.core.network.NetworkResult
import com.example.simplecurrencyexchange.domain.model.Currency
import com.example.simplecurrencyexchange.domain.model.Valute


fun ValuteResponse.toValute(): Valute =
    Valute(charCode, id, name, nominal, numCode, previous, value)

val listValute = listOf("USD", "EUR", "GBP")

fun CurrencyResponse.toCurrency(): Currency {
    return Currency(
//        valute = valute.map { it.value.toValute() }
        valute = valute.filterKeys { it in listValute }.map { it.value.toValute() }
    )
}

fun NetworkResult<CurrencyResponse>.toNetworkResultCurrency(): NetworkResult<Currency> {
    return when (this) {
        is NetworkResult.Success<CurrencyResponse> -> {
            NetworkResult.Success(this.data.toCurrency())
        }

        is NetworkResult.Error<CurrencyResponse> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}


