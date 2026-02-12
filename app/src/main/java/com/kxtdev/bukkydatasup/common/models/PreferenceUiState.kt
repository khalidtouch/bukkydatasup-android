package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfig
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrand

data class PreferenceUiState(
    val shouldEnableTransactionPin: Boolean = true,
    val shouldEnableBiometrics: Boolean = false,
    val isLoggedIn: Boolean = false,
    val themeBrand: ThemeBrand? = null,
    val useDynamicColor: Boolean? = null,
    val darkThemeConfig: DarkThemeConfig? = null,
    val shouldEnablePassCode: Boolean = true,
)