package com.kxtdev.bukkydatasup.modules.authentication.nav

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.enums.BiometricsResult
import com.kxtdev.bukkydatasup.common.enums.convertToSessionState
import com.kxtdev.bukkydatasup.common.utils.BiometricsPromptManager
import com.kxtdev.bukkydatasup.common.utils.NetworkCheckoutHandler
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.appToast
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.authentication.AuthenticationDetailScreen
import com.kxtdev.bukkydatasup.modules.authentication.AuthenticationScreen
import com.kxtdev.bukkydatasup.modules.authentication.vm.AuthenticationViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.navigateToHomeScreen
import com.kxtdev.bukkydatasup.modules.status.nav.navigateToStatusScreen
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.jumpBackTo
import com.kxtdev.bukkydatasup.navigation.poshComposable
import kotlinx.coroutines.delay

fun NavGraphBuilder.authenticationScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    exitApp: () -> Unit,
    authenticationViewModel: AuthenticationViewModel,
    biometricsPromptManager: BiometricsPromptManager,
    applicationContext: Context,
) {
    poshComposable(route = AppNavigation.AuthenticationScreen.route) {
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()
        val preferenceUiState by mainViewModel.preferenceUiState.collectAsStateWithLifecycle()
        val authenticationUiState by authenticationViewModel.authenticationUiState.collectAsStateWithLifecycle()
        val lastDestination by mainViewModel.lastDestination.collectAsStateWithLifecycle()

        val biometricsTitle = stringResource(id = R.string.unlock_to_login)
        val biometricsDescription = stringResource(R.string.scan_your_fingerprint)

        val biometricResult by biometricsPromptManager.promptResult.collectAsStateWithLifecycle(
            initialValue = null,
        )

        val context = LocalContext.current

        val onGetStarted: () -> Unit = {
            authenticationViewModel.updateAuthState(AuthState.WELCOME)
            mainViewModel.updateAuthState(AuthState.WELCOME)
        }

        val onLogin: () -> Unit = {
            mainViewModel.updateAuthState(AuthState.LOGIN)
            authenticationViewModel.updateAuthState(AuthState.LOGIN)
            navController.navigateToAuthenticationDetailScreen()
        }

        val onCreateAccount: () -> Unit = {
            mainViewModel.updateAuthState(AuthState.SIGNUP)
            authenticationViewModel.updateAuthState(AuthState.SIGNUP)
            navController.navigateToAuthenticationDetailScreen()
        }

        val onBackPressed: () -> Unit = {
            exitApp.invoke()
        }

        val updatePasscode: (String) -> Unit = { code ->
            authenticationViewModel.updatePasscode(code)
        }

        val onLogout: () -> Unit = {
            mainViewModel.logout(context) {
                mainViewModel.updateAuthState(AuthState.INTRODUCTION)
                authenticationViewModel.updateAuthState(AuthState.INTRODUCTION)
            }
        }

        val onHandleThrowable: () -> Unit = {
            authenticationViewModel.onHandledThrowable()
            mainViewModel.onHandledThrowable()
        }

        val onEngageBiometrics: () -> Unit = {
            biometricsPromptManager.showPrompt(
                biometricsTitle,
                biometricsDescription
            )
        }

        val onAction: () -> Unit = {
            authenticationViewModel.login(context)
        }

        val onAlternateAction: () -> Unit = {
            authenticationViewModel.updateAuthState(AuthState.SIGNUP)
            mainViewModel.updateAuthState(AuthState.SIGNUP)
            navController.navigateToAuthenticationDetailScreen()
        }

        val onForgotPassword: () -> Unit = {
            authenticationViewModel.forgotPassword(context)
        }

        LaunchedEffect(Unit) {
            if(appUiState.authState == AuthState.SPLASH_SCREEN) {
                delay(Settings.SPLASH_SCREEN_DELAY)
                val sessionState = mainViewModel.getLoggedInSessionState()
                val nextAuthState = sessionState?.convertToSessionState()?.getNextAuthState(preferenceUiState)
                    ?: AuthState.INTRODUCTION
                authenticationViewModel.updateAuthState(nextAuthState)
                mainViewModel.updateAuthState(nextAuthState)
            }
        }

        LaunchedEffect(authenticationUiState.passcode) {
            authenticationUiState.passcode?.let { p ->
                if(p.length >= Settings.MAX_TRANSACTION_PIN_LENGTH) {
                    authenticationViewModel.loginWithPasscode(context)
                }
            }
        }

        LaunchedEffect(authenticationUiState.isLoading) {
            if(authenticationUiState.canGoToDashboard == true) {
                navController.jumpBackTo(lastDestination)
                /*todo(): */
                authenticationViewModel.onOpenedDashboard()
            }
        }

        LaunchedEffect(biometricResult) {
            when(biometricResult) {
                is BiometricsResult.AuthenticationError -> {
                    appToast(applicationContext, "Error reading fingerprint")
                }
                is BiometricsResult.AuthenticationFailed -> {
                    appToast(applicationContext, "Failed")
                }
                is BiometricsResult.AuthenticationSuccess -> {
                    appToast(applicationContext, "Success!")
                    authenticationViewModel.loginWithBiometrics(context)
                }
                else -> Unit
            }
        }


        AuthenticationScreen(
            authState = appUiState.authState,
            onGetStarted = onGetStarted,
            onLogin = onLogin,
            onCreateAccount = onCreateAccount,
            authenticationUiState = authenticationUiState,
            preferenceUiState = preferenceUiState,
            updatePasscode = updatePasscode,
            onLogout = onLogout,
            onHandleThrowable = onHandleThrowable,
            onEngageBiometrics = onEngageBiometrics,
            updateUsername = authenticationViewModel::updateUsername,
            updatePassword = authenticationViewModel::updatePassword,
            onForgotPassword = onForgotPassword,
            onAction = onAction,
            onAlternateAction = onAlternateAction,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}

fun NavGraphBuilder.authenticationDetailScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    authenticationViewModel: AuthenticationViewModel,
) {
    poshComposable(route = AppNavigation.AuthenticationDetailScreen.route) {
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()
        val currentAuthenticationPage by authenticationViewModel.currentAuthenticationPage
            .collectAsStateWithLifecycle()
        val authenticationUiState by authenticationViewModel.authenticationUiState
            .collectAsStateWithLifecycle()
        val authenticationRequest by authenticationViewModel.authenticationRequest
            .collectAsStateWithLifecycle()

        val context = LocalContext.current

        val onBackPressed: () -> Unit = {
            when(appUiState.authState) {
                AuthState.LOGIN -> {
                    mainViewModel.updateAuthState(AuthState.WELCOME)
                    authenticationViewModel.updateAuthState(AuthState.WELCOME)
                    navController.popBackStack()
                }
                AuthState.SIGNUP -> {
                    if(currentAuthenticationPage <= 1) {
                        mainViewModel.updateAuthState(AuthState.WELCOME)
                        authenticationViewModel.updateAuthState(AuthState.WELCOME)
                        navController.popBackStack()
                    } else {
                        authenticationViewModel.previousPage()
                    }
                }
                else -> Unit
            }

        }

        val onForgotPassword: () -> Unit = {
            authenticationViewModel.forgotPassword(context)
        }

        val onAction: () -> Unit = {
            when(appUiState.authState) {
                AuthState.LOGIN -> {
                    authenticationViewModel.login(context)
                }
                AuthState.SIGNUP -> {
                    if(currentAuthenticationPage >= AuthenticationViewModel.TOTAL_AUTHENTICATION_PAGES) {
                        authenticationViewModel.register(context, mainViewModel::updateTransactionStatus)
                    } else {
                        authenticationViewModel.nextPage()
                    }
                }
                else -> Unit
            }
        }

        val onAlternateAction: () -> Unit = {
            when(appUiState.authState) {
                AuthState.LOGIN -> {
                    authenticationViewModel.updateAuthState(AuthState.SIGNUP)
                    mainViewModel.updateAuthState(AuthState.SIGNUP)
                }
                AuthState.SIGNUP -> {
                    authenticationViewModel.updateAuthState(AuthState.LOGIN)
                    mainViewModel.updateAuthState(AuthState.LOGIN)
                }
                else -> Unit
            }
        }

        val onTermsAndPrivacyClicked: () -> Unit = {
            NetworkCheckoutHandler.openPrivacyPolicy(context)
        }

        val onCheckedTermsAndPrivacyPolicy: (Boolean) -> Unit = { agree ->
            authenticationViewModel.updateTermsAndPrivacyPolicyAgreement(agree)
        }

        val onHandleThrowable: () -> Unit = {
            mainViewModel.onHandledThrowable()
            authenticationViewModel.onHandledThrowable()
        }

        LaunchedEffect(authenticationUiState.isLoading) {
            if(authenticationUiState.canGoToDashboard == true) {
                navController.navigateToHomeScreen()
                authenticationViewModel.onOpenedDashboard()
            }
            if(authenticationUiState.hasCompletedAccountCreation == true) {
                authenticationViewModel.onAccountCreationCompleted()
                navController.navigateToStatusScreen()
            }
        }

        LaunchedEffect(appUiState.authState) {
            onHandleThrowable.invoke()
        }

        AuthenticationDetailScreen(
            authState = appUiState.authState,
            onBackPressed = onBackPressed,
            onForgotPassword = onForgotPassword,
            onAction = onAction,
            onAlternateAction = onAlternateAction,
            currentAuthenticationPage = currentAuthenticationPage,
            onTermsAndPrivacyClicked = onTermsAndPrivacyClicked,
            onCheckedTermsAndPrivacyPolicy = onCheckedTermsAndPrivacyPolicy,
            authenticationUiState = authenticationUiState,
            onHandleThrowable = onHandleThrowable,
            updatePassword = authenticationViewModel::updatePassword,
            updateUsername = authenticationViewModel::updateUsername,
            updateConfirmPassword = authenticationViewModel::updateConfirmPassword,
            updatePhone = authenticationViewModel::updatePhone,
            updateEmail = authenticationViewModel::updateEmail,
            updateFullname = authenticationViewModel::updateFullName,
            updateAddress = authenticationViewModel::updateAddress,
            updateRefererUsername = authenticationViewModel::updateRefererUsername,
            authenticationRequest = authenticationRequest,
        )

        BackHandler {
            onBackPressed.invoke()
        }

    }
}


fun NavController.navigateToAuthenticationScreen() {
    navigate(AppNavigation.AuthenticationScreen.route) {
        popUpTo(AppNavigation.ProfileScreen.route) {
            inclusive = true
            saveState = false
        }
        launchSingleTop = true
        restoreState = true
    }
}


fun NavController.navigateToAuthenticationDetailScreen() {
    navigate(AppNavigation.AuthenticationDetailScreen.route)
}
