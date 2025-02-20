package com.example.simplecurrencyexchange.di

import android.content.Context
import androidx.room.Room
import com.example.simplecurrencyexchange.data.database.CurrencyDb
import com.example.simplecurrencyexchange.data.database.dao.CurrencyDao
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context, gson: Gson): CurrencyDb {

        return Room.databaseBuilder(
            context,
            CurrencyDb::class.java,
            "currency_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGetArticleDao(db: CurrencyDb): CurrencyDao {
        return db.getCurrencyDao()
    }

}

