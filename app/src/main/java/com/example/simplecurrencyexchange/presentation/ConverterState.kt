package com.example.simplecurrencyexchange.presentation

import com.example.simplecurrencyexchange.presentation.model.ValuteUI

//data class ConverterState(
//    val value: List<ValuteUI> = emptyList(),
//    val valuteTop: ValuteUI? = null,
//    val valuteBottom: ValuteUI?=null,
//    val result:Double?=null,
//    val symbol:String?=null,
//    val indexTop:Int? = null
//)

data class ConverterState(
    val itemsTopValute: List<ValuteUI> = emptyList(),
    val itemsBottomValute: List<ValuteUI> = emptyList(),
    val valuteTop:Int = 0,
    val valuteBottom:Int = 0,
    val resultTop:Double=0.0,
    val resultBottom:Double=0.0,
    val symbol:String="",
    val symbolAnotherCurrency:String="",
    val inputAmountTop:Double = 0.0,
    val inputAmountBottom:Double = 0.0
)