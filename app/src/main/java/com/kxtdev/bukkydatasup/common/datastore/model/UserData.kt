package com.kxtdev.bukkydatasup.common.datastore.model

class UserData(
    val useDynamicColor: Boolean = false,
    val themeBrand: ThemeBrand = ThemeBrandDefaults.DEFAULT,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfigDefaults.FOLLOW_SYSTEM,
    val shouldEnableTransactionPin: Boolean = false,
    val shouldEnableBiometrics: Boolean = true,
    val isLoggedIn: Boolean = false,
    val username: String? = null,
    val lastDestination: String? = null,
    val shouldLockScreen: Boolean = false,
    val shouldEnablePassCode: Boolean = false,
)