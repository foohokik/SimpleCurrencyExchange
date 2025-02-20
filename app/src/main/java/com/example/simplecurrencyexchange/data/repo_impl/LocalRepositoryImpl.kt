package com.example.simplecurrencyexchange.data.repo_impl

import com.example.simplecurrencyexchange.data.database.dao.CurrencyDao
import com.example.simplecurrencyexchange.data.model.BalanceData
import com.example.simplecurrencyexchange.domain.LocalRepository
import com.example.simplecurrencyexchange.domain.model.Valute
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val dao:CurrencyDao): LocalRepository {

    override suspend fun saveBalance(balanceData: BalanceData) {
        TODO("Not yet implemented")
    }

    override suspend fun getBalance(charCode: String): Valute {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBalances(): List<Valute> {
        TODO("Not yet implemented")
    }
}