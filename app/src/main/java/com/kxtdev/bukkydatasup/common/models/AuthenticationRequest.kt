package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.utils.Settings

data class AuthenticationRequest(
    val username: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val fullname: String? = null,
    val address: String? = null,
    val refererUsername: String? = null,
    val hasAgreedToTermsAndPrivacyPolicy: Boolean? = null,
    val authState: AuthState? = null,
) {
    val isLevelOneCompleted: Boolean get() {

        return !fullname.isNullOrBlank() &&
                fullname.length >= Settings.MIN_USERNAME_LENGTH &&
                !username.isNullOrBlank() &&
                username.length >= Settings.MIN_USERNAME_LENGTH &&
                !email.isNullOrBlank() &&
                email.length >= Settings.MIN_EMAIL_LENGTH &&
                !phone.isNullOrBlank() &&
                phone.length >= Settings.MIN_PHONE_LENGTH
    }

    val isLevelTwoCompleted: Boolean get() {

        return when(authState) {
            AuthState.SIGNUP -> {
                !address.isNullOrBlank() &&
                        address.length >= Settings.MIN_ADDRESS_LENGTH
            }
            else -> false
        }
    }

    val isLevelThreeCompleted: Boolean get() {

        return when(authState) {
            AuthState.SIGNUP -> {
                !password.isNullOrBlank() &&
                        password.length >= Settings.MIN_PASSWORD_LENGTH &&
                        !confirmPassword.isNullOrBlank() &&
                        confirmPassword.length >= Settings.MIN_PASSWORD_LENGTH &&
                        hasAgreedToTermsAndPrivacyPolicy == true
            }
            else -> false
        }
    }

    private val isValid: Boolean get() {
        return when(authState) {
            AuthState.LOGIN -> {
                !username.isNullOrBlank() &&
                        username.length >= Settings.MIN_USERNAME_LENGTH &&
                        !password.isNullOrBlank() &&
                        password.length >= Settings.MIN_PASSWORD_LENGTH
            }
            else -> false
        }
    }

    fun isValid(page: Int): Boolean {
        return when(authState) {
            AuthState.SIGNUP -> {
                when(page) {
                    1 -> isLevelOneCompleted
                    2 -> isLevelTwoCompleted
                    3 -> isLevelThreeCompleted
                    else -> false
                }
            }
            AuthState.LOGIN -> isValid
            else -> false
        }
    }
}