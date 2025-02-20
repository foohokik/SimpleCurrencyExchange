package com.example.simplecurrencyexchange.data.model

data class BalanceData(
    val isFirstEnter: Boolean,
    val balance: Map<String, Double>
)

