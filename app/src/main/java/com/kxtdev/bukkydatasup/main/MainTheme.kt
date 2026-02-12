package com.kxtdev.bukkydatasup.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfigDefaults
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrandDefaults
import com.kxtdev.bukkydatasup.common.models.PreferenceUiState


object MainTheme {
    @Composable
    fun shouldUseDarkTheme(
        preferenceUiState: PreferenceUiState
    ): Boolean {
        return when(preferenceUiState.darkThemeConfig) {
            DarkThemeConfigDefaults.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            DarkThemeConfigDefaults.LIGHT -> false
            DarkThemeConfigDefaults.DARK -> true
            else -> isSystemInDarkTheme()
        }
    }

    @Composable
    fun shouldUseAndroidTheme(
        preferenceUiState: PreferenceUiState
    ): Boolean {
        return when(preferenceUiState.themeBrand) {
            ThemeBrandDefaults.DEFAULT -> false
            ThemeBrandDefaults.ANDROID -> true
            else -> false
        }
    }

    @Composable
    fun shouldDisableDynamicTheming(
        preferenceUiState: PreferenceUiState
    ): Boolean {
        return preferenceUiState.useDynamicColor != true
    }
}