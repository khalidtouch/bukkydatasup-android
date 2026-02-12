package com.kxtdev.bukkydatasup.common.enums

import com.kxtdev.bukkydatasup.common.models.PreferenceUiState

enum class LoggedInSessionState {
    LOGGED_IN,
    LOGGED_OUT;

    fun getNextAuthState(preferenceUiState: PreferenceUiState): AuthState {
        return when(this) {
            LOGGED_IN -> if(preferenceUiState.shouldEnablePassCode) AuthState.QUICK_SIGN_IN
            else AuthState.LOGIN
            else -> AuthState.INTRODUCTION
        }
    }
}

fun String.convertToSessionState(): LoggedInSessionState {
    return when(this) {
        LoggedInSessionState.LOGGED_IN.name -> LoggedInSessionState.LOGGED_IN
        else -> LoggedInSessionState.LOGGED_OUT
    }
}