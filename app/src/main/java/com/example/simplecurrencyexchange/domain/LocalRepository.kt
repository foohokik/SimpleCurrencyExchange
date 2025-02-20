package com.example.simplecurrencyexchange.domain

import com.example.simplecurrencyexchange.data.model.BalanceData
import com.example.simplecurrencyexchange.domain.model.Valute

interface LocalRepository {

    suspend fun saveBalance (balanceData: BalanceData)
    suspend fun  getBalance (charCode: String): Valute
    suspend fun getAllBalances(): List<Valute>

}