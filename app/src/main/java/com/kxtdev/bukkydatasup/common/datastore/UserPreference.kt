package com.kxtdev.bukkydatasup.common.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfig
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfigDefaults
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrand
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrandDefaults
import com.kxtdev.bukkydatasup.common.datastore.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


const val BASE_NAME = "bukkydatasup"
val Context.userPreferencesDataSource by preferencesDataStore("${BASE_NAME}_user_preferences")

class UserPreferenceImpl @Inject constructor(
    private val pref: DataStore<Preferences>
): UserPreference {
    companion object {
        val KEY_THEME_BRAND = stringPreferencesKey("${BASE_NAME}_key_theme_brand")
        val KEY_DARK_THEME_CONFIG = stringPreferencesKey("${BASE_NAME}_key_dark_theme_config")
        val KEY_USE_DYNAMIC_COLOR = booleanPreferencesKey("${BASE_NAME}_key_use_dynamic_color")
        val KEY_SHOULD_ENABLE_TRANSACTION_PIN = booleanPreferencesKey("${BASE_NAME}_key_should_enable_transaction_pin")
        val KEY_SHOULD_ENABLE_BIOMETRICS = booleanPreferencesKey("${BASE_NAME}_should_enable_biometrics")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("${BASE_NAME}_key_is_logged_in")
        val KEY_USERNAME = stringPreferencesKey("${BASE_NAME}_key_username")
        val KEY_LAST_DESTINATION = stringPreferencesKey("${BASE_NAME}_key_last_destination")
        val KEY_LOCK_SCREEN = booleanPreferencesKey("${BASE_NAME}_key_lock_screen")
        val KEY_SHOULD_ENABLE_PASSCODE = booleanPreferencesKey("${BASE_NAME}_key_should_enable_passcode")
    }

    override val userData: Flow<UserData>
        get() = pref.data
            .catch {
                it.printStackTrace()
                emit(emptyPreferences())
            }.map { preference ->
                UserData(
                    themeBrand = preference[KEY_THEME_BRAND] ?: ThemeBrandDefaults.DEFAULT,
                    darkThemeConfig = preference[KEY_DARK_THEME_CONFIG] ?: DarkThemeConfigDefaults.FOLLOW_SYSTEM,
                    useDynamicColor = preference[KEY_USE_DYNAMIC_COLOR] ?: false,
                    shouldEnableTransactionPin = preference[KEY_SHOULD_ENABLE_TRANSACTION_PIN] ?: false,
                    shouldEnableBiometrics = preference[KEY_SHOULD_ENABLE_BIOMETRICS] ?: true,
                    isLoggedIn = preference[KEY_IS_LOGGED_IN] ?: false,
                    username = preference[KEY_USERNAME].orEmpty(),
                    lastDestination = preference[KEY_LAST_DESTINATION],
                    shouldLockScreen = preference[KEY_LOCK_SCREEN] ?: false,
                    shouldEnablePassCode = preference[KEY_SHOULD_ENABLE_PASSCODE] ?: true,

                )
            }

    override suspend fun setShouldEnableTransactionPin(enable: Boolean) {
        try {
            pref.edit { p -> p[KEY_SHOULD_ENABLE_TRANSACTION_PIN] = enable }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun updateDarkTheme(darkThemeConfig: DarkThemeConfig) {
        try {
            pref.edit { p -> p[KEY_DARK_THEME_CONFIG] = darkThemeConfig }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun updateThemeBrand(themeBrand: ThemeBrand) {
        try {
            pref.edit { p -> p[KEY_THEME_BRAND] = themeBrand }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        try {
            pref.edit { p -> p[KEY_USE_DYNAMIC_COLOR] = useDynamicColor }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun setShouldEnableBiometrics(enable: Boolean) {
        try {
            pref.edit { p -> p[KEY_SHOULD_ENABLE_BIOMETRICS] = enable }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun setIsLoggedIn(state: Boolean) {
        try {
            pref.edit { p -> p[KEY_IS_LOGGED_IN] = state }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun setUsername(username: String) {
        try {
            pref.edit { p -> p[KEY_USERNAME] = username }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun saveLastDestination(destination: String) {
        try {
            pref.edit { p -> p[KEY_LAST_DESTINATION] = destination }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun lockScreen(shouldLock: Boolean) {
        try {
            pref.edit { p -> p[KEY_LOCK_SCREEN] = shouldLock }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override suspend fun setShouldEnablePassCode(enable: Boolean) {
        try {
            pref.edit { p -> p[KEY_SHOULD_ENABLE_PASSCODE] = enable }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}

interface UserPreference {
    val userData: Flow<UserData>
    suspend fun setShouldEnableTransactionPin(enable: Boolean)
    suspend fun updateDarkTheme(darkThemeConfig: DarkThemeConfig)
    suspend fun updateThemeBrand(themeBrand: ThemeBrand)
    suspend fun updateDynamicColorPreference(useDynamicColor: Boolean)
    suspend fun setShouldEnableBiometrics(enable: Boolean)
    suspend fun setIsLoggedIn(state: Boolean)
    suspend fun setUsername(username: String)
    suspend fun saveLastDestination(destination: String)
    suspend fun lockScreen(shouldLock: Boolean)
    suspend fun setShouldEnablePassCode(enable: Boolean)
}

