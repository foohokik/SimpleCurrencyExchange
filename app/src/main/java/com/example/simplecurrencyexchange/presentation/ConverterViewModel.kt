package com.example.simplecurrencyexchange.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecurrencyexchange.core.network.onError
import com.example.simplecurrencyexchange.core.network.onException
import com.example.simplecurrencyexchange.core.network.onSuccess
import com.example.simplecurrencyexchange.di.IoDispatcher
import com.example.simplecurrencyexchange.domain.ConverterInteractor
import com.example.simplecurrencyexchange.presentation.adapter_top.AdapterListener
import com.example.simplecurrencyexchange.presentation.model.ValuteUI
import com.example.simplecurrencyexchange.presentation.model.toListValuteUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
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
                while (isActive) {
                    getAndMergeCurrenciesData()
                    convertCurrency()
                    delay(30000)
                }
            }
        }
    }

    private suspend fun getAndMergeCurrenciesData() {
        val resultValute = interactor.getCurrencies()
        resultValute.onSuccess { currency ->
            val listValute = currency.valute.toListValuteUI()
            val currentState = _stateFlow.value
            if (currentState.itemsTopValute.isNotEmpty() || currentState.itemsBottomValute.isNotEmpty()) {
                val updatedTop = currentState.itemsTopValute.map { topValute ->
                    val newValute = listValute.find { it.id == topValute.id }
                    newValute?.let {
                        topValute.copy(
                            value = it.value,
                            symbol = it.symbol,
                            balance = interactor.getBalance(it.charCode)?.toDouble() ?: 0.0
                        )
                    } ?: topValute
                }

                val updatedBottom = currentState.itemsBottomValute.map { bottomValute ->
                    val newValute = listValute.find { it.id == bottomValute.id }
                    newValute?.let {
                        bottomValute.copy(
                            value = it.value,
                            symbol = it.symbol,
                            balance = interactor.getBalance(it.charCode)?.toDouble() ?: 0.0
                        )
                    } ?: bottomValute
                }

                _stateFlow.update {
                    it.copy(itemsTopValute = updatedTop, itemsBottomValute = updatedBottom)
                }
            } else {

                val mergedResult = listValute.map { valuteUI ->
                    valuteUI.copy(
                        balance = interactor.getBalance(valuteUI.charCode)?.toDouble() ?: 0.0
                    )
                }
                _stateFlow.update {
                    it.copy(itemsTopValute = mergedResult, itemsBottomValute = mergedResult)
                }
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
        val amount = text.toDoubleOrNull() ?: return
        _stateFlow.update { state ->
            val (exchangeAmount, updatedItems) = calculateExchangeAmount(
                inputAmount = amount,
                exchangeRate = state.resultTop,
                items = state.itemsBottomValute,
                index = state.valuteBottom,
                updateField = { valute, amount -> valute.copy(convertationInputResultBottom = amount) }
            )
            state.copy(
                inputAmountTopEditFlow = amount,
                inputAmountTop = amount,
                inputAmountBottom = exchangeAmount,
                itemsBottomValute = updatedItems
            )
        }
    }

    override fun onEditTextChangeBottom(text: String) {
        val amount = text.toDoubleOrNull() ?: return

        _stateFlow.update { state ->
            val (exchangeAmount, updatedItems) = calculateExchangeAmount(
                inputAmount = amount,
                exchangeRate = state.resultBottom,
                items = state.itemsTopValute,
                index = state.valuteTop,
                updateField = { valute, amount -> valute.copy(convertationInputResultTop = amount) }
            )
            state.copy(
                inputAmountBottomEditFlow = amount,
                inputAmountBottom = amount,
                inputAmountTop = exchangeAmount,
                itemsTopValute = updatedItems
            )
        }
    }

    private fun calculateExchangeAmount(
        inputAmount: Double,
        exchangeRate: Double,
        items: List<ValuteUI>,
        index: Int,
        updateField: (ValuteUI, Double) -> ValuteUI
    ): Pair<Double, List<ValuteUI>> {
        val exchangeAmount = inputAmount * exchangeRate
        val updatedItems = items.mapIndexed { i, valute ->
            if (i == index) updateField(valute, exchangeAmount) else valute
        }
        return exchangeAmount to updatedItems
    }

    fun doExchange() {
        val state = _stateFlow.value
        val mutableListTop = state.itemsTopValute.toMutableList()
        val mutableListBottom = state.itemsBottomValute.toMutableList()

        val topValute = mutableListTop[state.valuteTop]
        val bottomValute = mutableListBottom[state.valuteBottom]

        val transferAmountCurrency = state.inputAmountTop
        val getAmountCurrency = state.inputAmountBottom
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

            val updatedTopValute = topValute.copy(balance = refactorResultBalanceTopValute, convertationInputResultTop = 0.0)
            val updatedBottomValute = bottomValute.copy(balance = refactorResultBalanceBottomValute, convertationInputResultBottom = 0.0)

            mutableListTop[state.valuteTop] = updatedTopValute
            mutableListTop[state.valuteBottom] = updatedBottomValute
            mutableListBottom[state.valuteBottom] = updatedBottomValute
            _stateFlow.update {
                it.copy(
                    itemsTopValute = mutableListTop,
                    itemsBottomValute = mutableListBottom
                )
            }
            _sideEffects.send(SideEffects.OnClickExchange(_stateFlow.value.itemsTopValute))
        }
    }

}