package com.example.simplecurrencyexchange.domain

import com.example.simplecurrencyexchange.core.network.NetworkResult
import com.example.simplecurrencyexchange.domain.model.Currency
import javax.inject.Inject

const val KEY_IS_FIRST_ENTER = "KEY_IS_FIRST_ENTER"

class ConverterInteractor @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val preferencesManager: PreferencesManager
) {

    private val isFirstEnter = preferencesManager.getBoolean(KEY_IS_FIRST_ENTER)

    suspend fun getCurrencies(): NetworkResult<Currency> {

        return if (isFirstEnter) {
            setInitialDataToSharedPref(balance)
            remoteRepository.getCurrencies()
        } else {
            remoteRepository.getCurrencies()
        }
    }

     fun getBalance(key:String): String? {
        return preferencesManager.getBalance(key)
    }

    private val balance: Map<String, Double> = mapOf("USD" to 100.0, "EUR" to 100.0, "GBP" to 100.0)

    private fun setInitialDataToSharedPref(
        balanceData: Map<String, Double>
    ) {
        preferencesManager.saveBoolean(KEY_IS_FIRST_ENTER, data = false)
        balanceData.forEach { (key, value) ->
            preferencesManager.saveBalance(key, value.toString())
        }
    }

     fun saveBalance (key: String, balance: String) {
        preferencesManager.saveBalance(key,balance)
    }

}