package com.example.simplecurrencyexchange.data

import android.content.SharedPreferences
import com.example.simplecurrencyexchange.data.model.BalanceData
import com.example.simplecurrencyexchange.domain.PreferencesManager
import com.google.gson.Gson
import javax.inject.Inject

class PreferencesManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : PreferencesManager {

    //    override fun saveData(key: String, data: BalanceData) {
//        val jsonString = gson.toJson(data)
//        sharedPreferences.edit().putString(key, jsonString).apply()
//    }
//
//    override fun  getData(key: String): BalanceData {
//        val jsonString = sharedPreferences.getString(key, null)
//        return gson.fromJson(jsonString, BalanceData::class.java)
//    }
//
//    override fun removeData(key: String) {
//        sharedPreferences.edit().remove(key).apply()
//    }
    override fun saveBalance(key: String, data: String) {
        sharedPreferences.edit().putString(key, data).apply()
    }

    override fun getBalance(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun saveBoolean(key: String, data: Boolean) {
        sharedPreferences.edit().putBoolean(key, data).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }


}