package com.example.simplecurrencyexchange.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "currency")
data class BalanceEntity(
    val charCode: String,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val balance: Double,
):Serializable

//@Entity(tableName = "currency")
//data class BalanceEntity(
//    @PrimaryKey(autoGenerate = false)
//    val charCode: String,
//    val id: String,
//    val name: String,
//    val nominal: Int,
//    val numCode: String,
//    val previous: Double,
//    val value: Double,
//    val symbol: String,
//    val balance: Double,
//):Serializable
