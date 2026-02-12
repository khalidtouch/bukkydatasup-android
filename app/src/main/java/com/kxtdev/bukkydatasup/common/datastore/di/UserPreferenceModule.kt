package com.kxtdev.bukkydatasup.common.datastore.di

import android.app.Application
import com.kxtdev.bukkydatasup.common.datastore.UserPreference
import com.kxtdev.bukkydatasup.common.datastore.UserPreferenceImpl
import com.kxtdev.bukkydatasup.common.datastore.userPreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserPreferenceModule {
    @Singleton
    @Provides
    fun provideUserPreferencesSerializer(application: Application): UserPreference {
        return UserPreferenceImpl(application.userPreferencesDataSource)
    }
}