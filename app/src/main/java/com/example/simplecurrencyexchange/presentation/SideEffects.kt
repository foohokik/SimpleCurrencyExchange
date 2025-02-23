package com.example.simplecurrencyexchange.presentation

import com.example.simplecurrencyexchange.presentation.model.ValuteUI


sealed class SideEffects  {
    data class ErrorEffect(val err: String): SideEffects()
    data class ExceptionEffect(val throwable: Throwable): SideEffects()
    data class OnClickExchange(val listValute: List<ValuteUI>): SideEffects()
    data class CautionEffect (val cautionMsg: String): SideEffects()
}
