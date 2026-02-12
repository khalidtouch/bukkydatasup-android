package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.enums.ResetMode

data class PreferenceScreenUiState(
    val loadingMessage: String? = null,
    val isLoading: Boolean? = null,
    val error: Throwable? = null,
    val apiResponse: String? = null,
    val resetMode: ResetMode? = ResetMode.PASSWORD_VERIFICATION,
    val shouldShowStatusPage: Boolean? = null,
    val preferenceItem: PreferenceItem? = null,
    val shouldShowThemeDialog: Boolean? = null,
) {
    val nextResetMode: ResetMode? get() {
        return when {
            preferenceItem == PreferenceItem.RESET_TRANSACTION_PIN -> ResetMode.NEW_PIN
            else -> when (resetMode) {
                ResetMode.PASSWORD_VERIFICATION -> ResetMode.NEW_PASSWORD
                ResetMode.NEW_PASSWORD -> ResetMode.NEW_PASSWORD_AGAIN
                else -> null
            }
        }
    }

    val prevResetMode: ResetMode get() {
        return when(resetMode) {
            ResetMode.NEW_PIN -> ResetMode.PASSWORD_VERIFICATION
            ResetMode.NEW_PIN_AGAIN -> ResetMode.NEW_PIN
            ResetMode.NEW_PASSWORD -> ResetMode.PASSWORD_VERIFICATION
            ResetMode.NEW_PASSWORD_AGAIN -> ResetMode.NEW_PASSWORD
            else -> ResetMode.PASSWORD_VERIFICATION
        }
    }

    val buttonLabel: String? get() {
        return when(resetMode) {
           ResetMode.PASSWORD_VERIFICATION -> "Verify Password"
            ResetMode.NEW_PASSWORD -> "Continue"
            ResetMode.NEW_PASSWORD_AGAIN -> "Reset Password"
            else -> null
        }
    }
}