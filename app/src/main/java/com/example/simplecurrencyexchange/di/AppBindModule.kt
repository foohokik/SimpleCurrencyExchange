package com.example.simplecurrencyexchange.di


import com.example.simplecurrencyexchange.data.PreferencesManagerImpl
import com.example.simplecurrencyexchange.data.repo_impl.RemoteRepositoryImpl
import com.example.simplecurrencyexchange.domain.PreferencesManager
import com.example.simplecurrencyexchange.domain.RemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface AppBindModule {

    @Binds
    fun repoImplToRepo (remoteRepositoryImpl: RemoteRepositoryImpl): RemoteRepository

    @Binds
    fun prefManager (preferencesManagerImpl: PreferencesManagerImpl): PreferencesManager
}