package com.example.simplecurrencyexchange.presentation



sealed class SideEffects  {
    data class ErrorEffect(val err: String): SideEffects()
    data class ExceptionEffect(val throwable: Throwable): SideEffects()
    data class onClickExchange(val state: ConverterState): SideEffects()
    data class CautionEffect (val cautionMsg: String): SideEffects()
}
