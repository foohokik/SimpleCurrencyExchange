package com.example.simplecurrencyexchange.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecurrencyexchange.core.network.onError
import com.example.simplecurrencyexchange.core.network.onException
import com.example.simplecurrencyexchange.core.network.onSuccess
import com.example.simplecurrencyexchange.di.IoDispatcher
import com.example.simplecurrencyexchange.domain.ConverterInteractor
import com.example.simplecurrencyexchange.presentation.adapter_top.AdapterListener
import com.example.simplecurrencyexchange.presentation.model.toListValuteUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val interactor: ConverterInteractor,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel(), AdapterListener {

    private val _stateFlow = MutableStateFlow(ConverterState())
    val stateFlow = _stateFlow.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch { _sideEffects.send(SideEffects.ExceptionEffect(throwable)) }
    }

    init {
        getCurrencies()
    }



    private fun getCurrencies() {
        viewModelScope.launch(exceptionHandler) {
            withContext(ioDispatcher) {
                while (true) {
                    getAndMergeCurrenciesData()
                    delay(30000)
                }
            }
        }
    }

    private suspend fun getAndMergeCurrenciesData() {
        val resultValute = interactor.getCurrencies()
        resultValute.onSuccess { currency ->
            val listValute = currency.valute.toListValuteUI()
            val mergedResult = listValute.map { valuteUI ->
                valuteUI.copy(
                    balance = interactor.getBalance(valuteUI.charCode)?.toDouble() ?: 0.0
                )
            }
            _stateFlow.update {
                it.copy(itemsTopValute = mergedResult, itemsBottomValute = mergedResult)
            }
        }
            .onError { _, message -> _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty())) }
            .onException { throwable -> _sideEffects.send(SideEffects.ExceptionEffect(throwable)) }
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
        val state = _stateFlow.value
        val mutableListTop = state.itemsTopValute.toMutableList()
        val mutableListBottom = state.itemsBottomValute.toMutableList()
        val indexTop = state.valuteTop
        val indexBottom = state.valuteBottom
        val topValute = mutableListTop[indexTop]
        val bottomValute = mutableListBottom[indexBottom]
        val resultTop = topValute.value / bottomValute.value
        val resultBottom = bottomValute.value / topValute.value
        mutableListTop.set(
            indexTop, topValute.copy(
                convertationResult = resultTop, symbolAnotherCurrency = bottomValute.symbol
            )
        )
        mutableListBottom.set(
            indexBottom, bottomValute.copy(
                convertationResult = resultBottom, symbolAnotherCurrency = topValute.symbol
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
        val state = _stateFlow.value
        val topValuteAmount = state.inputAmountTopEditFlow
        val exchangeForOneCurrency = state.resultTop
        val exchangeAmount = topValuteAmount * exchangeForOneCurrency
        val mutableListBottom = state.itemsBottomValute.toMutableList()
        val index = state.valuteBottom
        val bottomValute = mutableListBottom[index]
        mutableListBottom.set(
            index, bottomValute.copy(
                convertationInputResult = exchangeAmount
            )
        )
        _stateFlow.update {
            it.copy(
                inputAmountBottom = exchangeAmount, itemsBottomValute = mutableListBottom
            )
        }
    }


    private fun countAmountTop() {
        val state = _stateFlow.value
        val bottomValuteAmount = state.inputAmountBottomEditFlow
        val exchangeForOneCurrency = state.resultBottom
        val exchangeAmount = bottomValuteAmount * exchangeForOneCurrency
        val mutableListTop = state.itemsTopValute.toMutableList()
        val index = state.valuteTop
        val topValute = mutableListTop[index]
        mutableListTop.set(
            index, topValute.copy(
                convertationInputResult = exchangeAmount
            )
        )
        _stateFlow.update {
            it.copy(
                inputAmountTop = exchangeAmount, itemsTopValute = mutableListTop
            )
        }
    }

    fun doExchange() {
        val state = _stateFlow.value
        val mutableListTop = state.itemsTopValute.toMutableList()
        val mutableListBottom = state.itemsBottomValute.toMutableList()

        val topValute = mutableListTop[state.valuteTop]
        val bottomValute = mutableListBottom[state.valuteBottom]

        val transferAmountCurrency = topValute.convertationInputResult
        val getAmountCurrency = bottomValute.convertationInputResult

        val keyTopValute = topValute.charCode
        val keyBottomValute = bottomValute.charCode

        viewModelScope.launch {
            val getTopValuteBalance = interactor.getBalance(keyTopValute)?.toDouble()
            val getBottomValuteBalance = interactor.getBalance(keyBottomValute)?.toDouble()

            if (getTopValuteBalance == null || getBottomValuteBalance == null) {
                _sideEffects.send(SideEffects.CautionEffect(""))
                return@launch
            }

            if (transferAmountCurrency > getTopValuteBalance) {
                _sideEffects.send(SideEffects.CautionEffect(""))
                return@launch
            }

            val refactorResultBalanceTopValute = getTopValuteBalance - transferAmountCurrency
            val refactorResultBalanceBottomValute = getBottomValuteBalance + getAmountCurrency

            interactor.saveBalance(keyTopValute, refactorResultBalanceTopValute.toString())
            interactor.saveBalance(keyBottomValute, refactorResultBalanceBottomValute.toString())

            val updatedTopValute = topValute.copy(balance = refactorResultBalanceTopValute)
            val updatedBottomValute = bottomValute.copy(balance = refactorResultBalanceBottomValute)

            mutableListTop[state.valuteTop] = updatedTopValute
            mutableListBottom[state.valuteBottom] = updatedBottomValute

            _stateFlow.update {
                it.copy(
                    itemsTopValute = mutableListTop,
                    itemsBottomValute = mutableListBottom
                )
            }

            _sideEffects.send(SideEffects.onClickExchange(_stateFlow.value))
        }
    }

}