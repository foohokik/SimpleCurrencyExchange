package com.example.simplecurrencyexchange.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simplecurrencyexchange.data.database.dao.CurrencyDao
import com.example.simplecurrencyexchange.data.database.entity.BalanceEntity


@Database(
    entities = [BalanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDb(): RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao

}