package com.example.simplecurrencyexchange.domain


interface PreferencesManager {

    fun saveBalance (key: String, data: String)
    fun getBalance (key: String): String?
    fun saveBoolean (key: String, data: Boolean)
    fun getBoolean (key: String): Boolean
}