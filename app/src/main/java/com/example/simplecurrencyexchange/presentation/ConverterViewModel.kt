package com.example.simplecurrencyexchange.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecurrencyexchange.core.network.onSuccess
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
               getAndMergeCurrenciesData()
                delay(300000)
            }
        }
    }

    private suspend fun getAndMergeCurrenciesData () {
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

    override fun onEditTextChangeTop(text: String) {
        _stateFlow.update { it.copy(inputAmountTopEditFlow = text.toDouble()) }
        countAmountBottom()
    }

    override fun onEditTextChangeBottom(text: String) {
        _stateFlow.update { it.copy(inputAmountBottomEditFlow = text.toDouble()) }
        countAmountTop()
    }

    private fun countAmountBottom() {
        val topValuteAmount = _stateFlow.value.inputAmountTopEditFlow
        val exchangeForOneCurrency = _stateFlow.value.resultTop
        val exchangeAmount = topValuteAmount * exchangeForOneCurrency
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
    }


    private fun countAmountTop() {
        val bottomValuteAmount = _stateFlow.value.inputAmountBottomEditFlow
        val exchangeForOneCurrency = _stateFlow.value.resultBottom
        val exchangeAmount = bottomValuteAmount * exchangeForOneCurrency
        val mutableListTop = _stateFlow.value.itemsTopValute.toMutableList()
        val index = _stateFlow.value.valuteTop
        val topValute = mutableListTop[index]
        mutableListTop.set(
            index, topValute.copy(
                convertationInputResult = exchangeAmount
            )
        )
        _stateFlow.update {
            it.copy(
                inputAmountTop = exchangeAmount,
                itemsTopValute = mutableListTop
            )
        }
    }

    fun doExchange () {
        val mutableListTop = _stateFlow.value.itemsTopValute.toMutableList()
        val mutableListBottom = _stateFlow.value.itemsBottomValute.toMutableList()
        val topValute = mutableListTop[_stateFlow.value.valuteTop]
        val bottomValute = mutableListBottom[_stateFlow.value.valuteBottom]
        val transferAmountCurrency = topValute.convertationInputResult
        val getAmountCurrency = bottomValute.convertationInputResult
        val keyTopValute = topValute.charCode
        val keyBottomValute = bottomValute.charCode
        viewModelScope.launch(ioDispatcher) {
            val getTopValuteBalance = interactor.getBalance(keyTopValute)
            val getBottomValuteBalance = interactor.getBalance(keyBottomValute)
            val refactorResultBalanceTopValute = getTopValuteBalance?.toDouble()
                ?.minus(transferAmountCurrency)
            val refactorResultBalanceBottomValute = getBottomValuteBalance?.toDouble()?.plus(getAmountCurrency)
            interactor.saveBalance(keyTopValute, refactorResultBalanceTopValute.toString())
            interactor.saveBalance(keyBottomValute, refactorResultBalanceBottomValute.toString())
            getAndMergeCurrenciesData()
        }

    }


}