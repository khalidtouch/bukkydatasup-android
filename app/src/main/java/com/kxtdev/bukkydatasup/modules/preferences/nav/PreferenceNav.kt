package com.kxtdev.bukkydatasup.modules.preferences.nav

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfig
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrand
import com.kxtdev.bukkydatasup.common.enums.BiometricsResult
import com.kxtdev.bukkydatasup.common.enums.ChildPreferenceItem
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.utils.BiometricsPromptManager
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.appToast
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.preferences.PreferenceDetailScreen
import com.kxtdev.bukkydatasup.modules.preferences.PreferenceScreen
import com.kxtdev.bukkydatasup.modules.profile.nav.navigateToProfileScreen
import com.kxtdev.bukkydatasup.modules.reset.nav.navigateToResetScreen
import com.kxtdev.bukkydatasup.modules.status.nav.navigateToStatusScreen
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.poshComposable
import kotlinx.coroutines.delay

fun NavGraphBuilder.preferenceScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    biometricsPromptManager: BiometricsPromptManager,
    applicationContext: Context,
) {
    poshComposable(route = AppNavigation.PreferenceScreen.route) {
        val preferenceUiState by mainViewModel.preferenceUiState.collectAsStateWithLifecycle()
        val preferenceScreenUiState by mainViewModel.preferenceScreenUiState.collectAsStateWithLifecycle()

        val biometricsTitle = stringResource(id = R.string.unlock_to_activate_biometrics)
        val biometricsDescription = stringResource(R.string.scan_your_fingerprint)

        val biometricResult by biometricsPromptManager.promptResult.collectAsStateWithLifecycle(
            initialValue = null,
        )
        val biometricsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { res -> }

        val onBackPressed: () -> Unit = {
            navController.navigateToProfileScreen()
        }

        val viewDetail: (PreferenceItem) -> Unit = { item ->
            when(item) {
                PreferenceItem.RESET_TRANSACTION_PIN,
                    PreferenceItem.RESET_PASSWORD -> {
                        mainViewModel.updatePreferenceItem(item)
                        navController.navigateToResetScreen()
                    }
                PreferenceItem.THEMES -> {
                    mainViewModel.updateThemeDialogState(true)
                }
                else -> Unit
            }
        }

        val onToggleCheckbox: (Boolean, PreferenceItem) -> Unit = {
            currentState, item ->
            when(item) {
                PreferenceItem.ENABLE_TRANSACTION_PIN -> {
                    mainViewModel.onToggleShouldEnableTransaction(currentState)
                }
                PreferenceItem.BIOMETRICS -> {
                    biometricsPromptManager.showPrompt(
                        title = biometricsTitle,
                        description = biometricsDescription
                    )
                }
                PreferenceItem.ENABLE_PASSCODE -> {
                    mainViewModel.onToggleShouldEnablePassCode(currentState)
                }
                else -> Unit
            }
        }

        val onFinishBiometrics: suspend () -> Unit = {
            delay(Settings.BIOMETRICS_DELAY)
            navController.popBackStack()
        }

        val onDismissThemeDialog: () -> Unit = {
            mainViewModel.updateThemeDialogState(null)
        }

        val onChangeDynamicColorPreference: (Boolean) -> Unit = { useDynamicColor ->
            mainViewModel.updateDynamicColorPreference(useDynamicColor)
        }

        val onChangeThemeBrand: (ThemeBrand) -> Unit = { brand ->
            mainViewModel.updateThemeBrand(brand)
        }

        val onChangeDarkThemeConfig: (DarkThemeConfig) -> Unit = { config ->
            mainViewModel.updateDarkThemeConfig(config)
        }

        LaunchedEffect(biometricResult) {
            when(biometricResult) {
                is BiometricsResult.AuthenticationNotSet -> {
                    if(Build.VERSION.SDK_INT >= 30) {
                        val enrollIntent = Intent(android.provider.Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                android.provider.Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                        biometricsLauncher.launch(enrollIntent)
                    }
                }
                is BiometricsResult.AuthenticationError -> {
                    appToast(applicationContext, "Error reading fingerprint")
                }
                is BiometricsResult.AuthenticationFailed -> {
                    appToast(applicationContext, "Failed")
                }
                is BiometricsResult.AuthenticationSuccess -> {
                    val currentState = preferenceUiState.shouldEnableBiometrics
                    mainViewModel.onToggleShouldEnableBiometrics(!currentState)
                    appToast(applicationContext, "Success!")
                    onFinishBiometrics.invoke()
                }
                is BiometricsResult.FeatureUnavailable -> {
                    appToast(applicationContext, "Unavailable")
                }
                is BiometricsResult.HardwareUnavailable -> {
                    appToast(applicationContext, "Fingerprint Hardware Unavailable")
                }
                else -> Unit
            }
        }

        PreferenceScreen(
            onBackPressed = onBackPressed,
            viewDetail = viewDetail,
            onToggleCheckbox = onToggleCheckbox,
            preferenceUiState = preferenceUiState,
            onDismissThemeDialog = onDismissThemeDialog,
            onChangeThemeBrand = onChangeThemeBrand,
            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
            onChangeDynamicColorPreference = onChangeDynamicColorPreference,
            preferenceScreenUiState = preferenceScreenUiState,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}

fun NavGraphBuilder.preferenceDetailScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
) {
    poshComposable(route = AppNavigation.PreferenceDetailScreen.route) {
        val preferenceScreenUiState by mainViewModel.preferenceScreenUiState.collectAsStateWithLifecycle()
        val loggedInUserState by mainViewModel.loggedInUserState.collectAsStateWithLifecycle()
        val securityRequest by mainViewModel.securityRequest.collectAsStateWithLifecycle()

        val onClickPreferenceDetailItem: (ChildPreferenceItem) -> Unit = { item ->
            mainViewModel.upgradeUser(item.title, mainViewModel::updateTransactionStatus)
        }

        val onBackPressed: () -> Unit = {
            when(preferenceScreenUiState.preferenceItem) {
                PreferenceItem.UPGRADE_ACCOUNT,
                    PreferenceItem.VERIFY_ACCOUNT -> {
                        navController.navigateToProfileScreen()
                    }
                else -> {
                    navController.navigateToPreferenceScreen()
                }
            }
        }

        val updateFullname: (String) -> Unit = { name ->
            mainViewModel.updateFullname(name)
        }

        val updateBvn: (String) -> Unit = { bvn ->
            mainViewModel.updateBvn(bvn)
        }

        val updateNin: (String) -> Unit = { nin ->
            mainViewModel.updateNin(nin)
        }

        val onVerifyAccount: () -> Unit = {
            mainViewModel.verifyReservedAccounts(mainViewModel::updateTransactionStatus)
        }

        LaunchedEffect(preferenceScreenUiState.shouldShowStatusPage) {
            if(preferenceScreenUiState.shouldShowStatusPage == true) {
                navController.navigateToStatusScreen()
                mainViewModel.updateStatusPageAs(null)
            }
        }


        PreferenceDetailScreen(
            preferenceScreenUiState = preferenceScreenUiState,
            onBackPressed = onBackPressed,
            loggedInUserState = loggedInUserState,
            onClickPreferenceDetailItem = onClickPreferenceDetailItem,
            updateFullname = updateFullname,
            updateBvn = updateBvn,
            securityRequest = securityRequest,
            onVerifyAccount = onVerifyAccount,
            updateNin = updateNin,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}


fun NavController.navigateToPreferenceScreen() {
    navigate(AppNavigation.PreferenceScreen.route)
}

fun NavController.navigateToPreferenceDetailScreen() {
    navigate(AppNavigation.PreferenceDetailScreen.route)
}
