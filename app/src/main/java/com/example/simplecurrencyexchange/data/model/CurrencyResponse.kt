package com.example.simplecurrencyexchange.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("Valute")
    val valute: Map<String, ValuteResponse>
)