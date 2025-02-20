package com.example.simplecurrencyexchange.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.core.network.onSuccess
import com.example.simplecurrencyexchange.core.extension.safeDiv
import com.example.simplecurrencyexchange.di.IoDispatcher
import com.example.simplecurrencyexchange.domain.ConverterInteractor
import com.example.simplecurrencyexchange.presentation.adapter_top.AdapterListener
import com.example.simplecurrencyexchange.presentation.model.toListValuteUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val interactor: ConverterInteractor,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel(), AdapterListener {

    private val _stateFlow = MutableStateFlow(ConverterState())
    val stateFlow = _stateFlow.asStateFlow()


    init {
        getCurrencies()

    }

    private fun getCurrencies() {
        viewModelScope.launch(ioDispatcher) {
            while (true) {
                val resultValute = interactor.getCurrencies()
                resultValute.onSuccess { currency ->
                    val listValute = currency.valute.toListValuteUI()
                    val mergedResult = listValute.map { valuteUI ->
                        valuteUI.copy(
                            balance = interactor.getBalance(valuteUI.charCode)?.toDouble()
                        )
                    }
                    _stateFlow.update {
                        ConverterState(
                            itemsTopValute = mergedResult,
                            itemsBottomValute = mergedResult
                        )
                    }
                }
                delay(300000)
            }
        }
    }

    fun topUpdateFlow(index: Int) {
        _stateFlow.update { it.copy(valuteTop = index) }
        convertCurrency()
    }

    fun bottomUpdateFlow(index: Int) {
        _stateFlow.update { it.copy(valuteBottom = index) }
        convertCurrency()
    }


    private fun convertCurrency() {
        val mutableListTop = _stateFlow.value.itemsTopValute.toMutableList()
        val mutableListBottom = _stateFlow.value.itemsBottomValute.toMutableList()
        val topValute = mutableListTop[_stateFlow.value.valuteTop]
        val bottomValute = mutableListBottom[_stateFlow.value.valuteBottom]
        val resultTop = topValute.value / bottomValute.value
        val resultBottom = bottomValute.value / topValute.value
        mutableListTop.set(
            _stateFlow.value.valuteTop,
            topValute.copy(
                convertationResult = resultTop,
                symbolAnotherCurrency = bottomValute.symbol
            )
        )
        mutableListBottom.set(
            _stateFlow.value.valuteBottom,
            bottomValute.copy(
                convertationResult = resultBottom,
                symbolAnotherCurrency = topValute.symbol
            )
        )
        _stateFlow.update {
            it.copy(
                itemsBottomValute = mutableListBottom,
                itemsTopValute = mutableListTop,
                resultTop = resultTop,
                resultBottom = resultBottom,
                symbol = topValute.symbol,
                symbolAnotherCurrency = bottomValute.symbol
            )
        }
    }

    override fun onEditTextChange(text: String) {
        _stateFlow.update { it.copy(inputAmountTop = text.toDouble()) }
        countAmountBottom()
    }

    private fun countAmountBottom() {
        val topValuteAmount = _stateFlow.value.inputAmountTop
        val exchangeForOneCurrency = _stateFlow.value.resultBottom
        val exchangeAmount = topValuteAmount * exchangeForOneCurrency
        Log.d("MyTag", "exchangeAmount  =  " + exchangeAmount)
        val mutableListBottom = _stateFlow.value.itemsBottomValute.toMutableList()
        val index = _stateFlow.value.valuteBottom
        val bottomValute = mutableListBottom[index]
        mutableListBottom.set(
            index, bottomValute.copy(
                convertationInputResult = exchangeAmount
            )
        )
        _stateFlow.update {
            it.copy(
                inputAmountBottom = exchangeAmount,
                itemsBottomValute = mutableListBottom
            )
        }
        Log.d("MyTag", "itemsBottomValute  =  " + _stateFlow.value.itemsBottomValute)
    }

//    private fun convertCurrencyBottom() {
//        val mutableList = _stateFlow.value.value.toMutableList()
//        val topValute = mutableList[_stateFlow.value.valuteTop]
//        val bottomValute = mutableList[_stateFlow.value.valuteBottom]
//        val resultBottom = bottomValute.value.safeDiv(topValute.value)
//        mutableList.set(
//            _stateFlow.value.valuteTop,
//            topValute.copy(convertationTopResult = resultBottom)
//        )
//        _stateFlow.update { it.copy(value = mutableList) }
//    }
}