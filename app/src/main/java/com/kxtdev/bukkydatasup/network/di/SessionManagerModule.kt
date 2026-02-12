package com.kxtdev.bukkydatasup.network.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.kxtdev.bukkydatasup.network.utils.NetworkSessionManager
import com.kxtdev.bukkydatasup.network.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    @Provides
    fun provideSessionManager(application: Application): SessionManager {
        return NetworkSessionManager(
            AuthSharedPreferencesFactory.sessionPreferences(
                application
            )
        )
    }

}

object AuthSharedPreferencesFactory {
    private const val FILE_NAME = "auth_shared_pref"
    fun sessionPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }
}