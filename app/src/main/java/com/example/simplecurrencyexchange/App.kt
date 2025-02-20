package com.example.simplecurrencyexchange

import android.app.Application
import com.example.simplecurrencyexchange.di.AppComponent
import com.example.simplecurrencyexchange.di.DaggerAppComponent


class App : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

}