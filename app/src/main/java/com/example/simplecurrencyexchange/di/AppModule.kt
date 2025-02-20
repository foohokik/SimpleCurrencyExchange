package com.example.simplecurrencyexchange.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides

@Module
class AppModule  {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

}