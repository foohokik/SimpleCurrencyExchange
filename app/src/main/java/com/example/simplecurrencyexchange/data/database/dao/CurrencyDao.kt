package com.example.simplecurrencyexchange.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simplecurrencyexchange.data.database.entity.BalanceEntity

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValute(valuteEntity: BalanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValutes(valute: List<BalanceEntity>)

    @Query ("SELECT * FROM currency WHERE id = :id")
    suspend fun getValuteById (id: String): BalanceEntity

    @Query("SELECT * FROM currency")
    suspend fun getAllValutes(): List<BalanceEntity>



}