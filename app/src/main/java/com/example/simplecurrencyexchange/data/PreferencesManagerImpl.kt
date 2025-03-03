package com.example.simplecurrencyexchange.data

import android.content.SharedPreferences
import com.example.simplecurrencyexchange.domain.PreferencesManager
import javax.inject.Inject

class PreferencesManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PreferencesManager {

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