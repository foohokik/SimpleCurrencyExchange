package com.example.simplecurrencyexchange.domain.model


data class Valute(
    val charCode: String,
    val id: String,
    val name: String,
    val nominal: Int,
    val numCode: String,
    val previous: Double,
    val value: Double,
    val balance: Double? = null
)