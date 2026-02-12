package com.kxtdev.bukkydatasup.modules.reset.nav

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.enums.ResetMode
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.preferences.nav.navigateToPreferenceScreen
import com.kxtdev.bukkydatasup.modules.reset.ResetScreen
import com.kxtdev.bukkydatasup.modules.status.nav.navigateToStatusScreen
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.poshComposable

fun NavGraphBuilder.resetScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    poshComposable(route = AppNavigation.ResetScreen.route) {
        val preferenceScreenUiState by mainViewModel.preferenceScreenUiState.collectAsStateWithLifecycle()
        val securityRequest by mainViewModel.securityRequest.collectAsStateWithLifecycle()
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()

        val onContinue: (ResetMode?) -> Unit = { next ->
            if(next == null) {
                mainViewModel.resetPassword(mainViewModel::updateTransactionStatus)
            } else {
                mainViewModel.updateResetMode(next)
            }
        }

        val updatePassword: (String) -> Unit = { p ->
            preferenceScreenUiState.resetMode?.let { mode ->
                mainViewModel.updateResetPassword(p, mode)
            }
        }

        val updateTransactionPin: (String) -> Unit = { p ->
            preferenceScreenUiState.resetMode?.let { mode ->
                mainViewModel.updateTransactionPin(p, mode)
            }
        }

        val onBackPressed: () -> Unit = {
            if(appUiState.authState != AuthState.SIGNUP) {
                when(preferenceScreenUiState.resetMode) {
                    ResetMode.PASSWORD_VERIFICATION -> {
                        navController.navigateToPreferenceScreen()
                    }
                    else -> {
                        mainViewModel.updateResetMode(preferenceScreenUiState.prevResetMode)
                    }
                }
            }
        }

        LaunchedEffect(preferenceScreenUiState.shouldShowStatusPage) {
            if(preferenceScreenUiState.shouldShowStatusPage == true) {
                navController.navigateToStatusScreen()
                mainViewModel.updateStatusPageAs(null)
            }
        }

        LaunchedEffect(Unit) {
            when {
                appUiState.authState == AuthState.SIGNUP -> {
                    val password = mainViewModel.getSavedPassword()
                    if(password.isNullOrBlank()) {
                        mainViewModel.updateResetMode(ResetMode.PASSWORD_VERIFICATION)
                    } else {
                        mainViewModel.updateResetMode(ResetMode.NEW_PIN)
                    }
                }
                else -> {
                    mainViewModel.updateResetMode(ResetMode.PASSWORD_VERIFICATION)
                }
            }
        }

        LaunchedEffect(
            securityRequest.pin1,
            securityRequest.pin2,
        ) {
            securityRequest.pin1?.let { p ->
                if(p.length >= Settings.MAX_TRANSACTION_PIN_LENGTH) {
                    mainViewModel.updateResetMode(ResetMode.NEW_PIN_AGAIN)
                }
            }
            securityRequest.pin2?.let { p ->
                if(p.length >= Settings.MAX_TRANSACTION_PIN_LENGTH) {
                    mainViewModel.resetTransactionPin(mainViewModel::updateTransactionStatus)
                }
            }
        }

        ResetScreen(
            onBackPressed = onBackPressed,
            preferenceScreenUiState = preferenceScreenUiState,
            updatePassword = updatePassword,
            onContinue = onContinue,
            updateTransactionPin = updateTransactionPin,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}



fun NavController.navigateToResetScreen() {
    navigate(AppNavigation.ResetScreen.route)
}