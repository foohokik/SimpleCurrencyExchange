package com.example.simplecurrencyexchange.presentation.model


data class ValuteUI(
    val charCode: String,
    val id: String,
    val name: String,
    val nominal: Int,
    val numCode: String,
    val previous: Double,
    val value: Double,
    val symbol: String,
    val balance: Double = 0.0,
    val convertationResult: Double = 0.0,
    val symbolAnotherCurrency: String ="",
    val convertationInputResultTop: Double = 0.0,
    val convertationInputResultBottom: Double = 0.0
)

