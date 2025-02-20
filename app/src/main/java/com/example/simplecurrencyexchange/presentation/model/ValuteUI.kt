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
    val balance: Double? = null,
    val convertationResult:Double = 0.0,
    val symbolAnotherCurrency: String? = null,
    val convertationInputResult: Double = 0.0
   // val convertationBottomResult:Double? = null
)

