package com.example.simplecurrencyexchange.presentation.model

import com.example.appnews.core.network.NetworkResult
import com.example.simplecurrencyexchange.domain.model.Currency
import com.example.simplecurrencyexchange.domain.model.Valute


fun Valute.toValuteUI(): ValuteUI = ValuteUI(
    charCode,
    id,
    name,
    nominal,
    numCode,
    previous,
    value,
    symbol = symbols.getValue(charCode)
)

fun List<Valute>.toListValuteUI(): List<ValuteUI> = this.map { it.toValuteUI() }

val symbols = mapOf(
    "GBP" to "£",
    "USD" to "$",
    "EUR" to "€",
    "RUB" to "₽"
)

fun Currency.toCurrencyUI(): CurrencyUI = CurrencyUI(
    valute = valute.toListValuteUI()
)

fun NetworkResult<Currency>.toNetworkResultCurrencyUI(): NetworkResult<CurrencyUI> {
    return when (this) {
        is NetworkResult.Success<Currency> -> {
            NetworkResult.Success(this.data.toCurrencyUI())
        }

        is NetworkResult.Error<Currency> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}
