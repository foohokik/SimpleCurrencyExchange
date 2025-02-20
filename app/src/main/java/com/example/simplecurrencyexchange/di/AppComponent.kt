package com.example.simplecurrencyexchange.di

import android.content.Context
import com.example.simplecurrencyexchange.presentation.ConverterFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [
    NetworkModule::class,
    AppBindModule::class,
    DispatchersModule::class,
    SharedPreferencesModule::class,
    AppModule::class])
@Singleton
interface AppComponent {

    fun inject(mainFragment: ConverterFragment )


    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}