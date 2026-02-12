package com.kxtdev.bukkydatasup.modules.home.nav

import android.content.ClipData
import android.content.ClipboardManager
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.utils.WhatsAppHandler
import com.kxtdev.bukkydatasup.common.utils.appToast
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.fund.nav.navigateToFundScreen
import com.kxtdev.bukkydatasup.modules.fund.vm.FundViewModel
import com.kxtdev.bukkydatasup.modules.home.HomeScreen
import com.kxtdev.bukkydatasup.modules.preferences.nav.navigateToPreferenceDetailScreen
import com.kxtdev.bukkydatasup.modules.preferences.nav.navigateToPreferenceScreen
import com.kxtdev.bukkydatasup.modules.transaction.nav.navigateToTransactionScreen
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionViewModel
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.TopLevelDestinations
import com.kxtdev.bukkydatasup.navigation.poshComposable


fun NavGraphBuilder.homeScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    clipboardManager: ClipboardManager,
    transactionViewModel: TransactionViewModel,
    fundViewModel: FundViewModel,
    onExitApp: () -> Unit,
) {
    poshComposable(route = AppNavigation.HomeScreen.route) {
        val loggedInUserState by mainViewModel.loggedInUserState.collectAsStateWithLifecycle()
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()
        val banksFlow by fundViewModel.banksFlowCached.collectAsStateWithLifecycle()
        val preferenceUiState by mainViewModel.preferenceUiState.collectAsStateWithLifecycle()
        val ads by mainViewModel.advertisements.collectAsStateWithLifecycle()

        val context = LocalContext.current
        val supportPlaceholder = context.getString(R.string.support_placeholder)

        val goToFundWallet: () -> Unit = {
            navController.navigateToFundScreen()
        }

        val onChatSupport: () -> Unit = {
            WhatsAppHandler().apply {
                chatSupport(
                    context,
                    loggedInUserState.userConfig.supportPhoneNumber,
                    supportPlaceholder
                )
            }
        }

        val onCopyAccountNumber: (String) -> Unit = { data ->
            val accountNumberData = ClipData.newPlainText("accountNumber", data)
            clipboardManager.setPrimaryClip(accountNumberData)
            appToast(context, context.getString(R.string.copied))
        }

        val onGenerateAccount: () -> Unit = {
            mainViewModel.updatePreferenceItem(PreferenceItem.VERIFY_ACCOUNT)
            navController.navigateToPreferenceDetailScreen()
        }

        val onBackPressed: () -> Unit = {
            onExitApp.invoke()
        }

        val onClickProduct: (Product) -> Unit = { prod ->
            when(prod) {
                Product.WHATSAPP_GROUP -> {
                    WhatsAppHandler().apply {
                        joinGroupChat(context, loggedInUserState.userConfig.groupLink)
                    }
                }
                else -> {
                    mainViewModel.selectProduct(prod)
                    transactionViewModel.updateTransactionProduct(prod)
                    transactionViewModel.updateFromDestination(AppNavigation.HomeScreen.route)
                    navController.navigateToTransactionScreen()
                }
            }
        }

        val onSetTransactionPin: () -> Unit = {
            navController.navigateToPreferenceScreen()
        }

        LaunchedEffect(Unit) {
            mainViewModel.getAlertNotification()
            mainViewModel.updateFCMToken()
            mainViewModel.refreshUser()
            mainViewModel.getDisabledServices()
            mainViewModel.getDisabledNetworks()
            mainViewModel.updateAppInitializationState(true)
            transactionViewModel.initTransactionRequest(appUiState.product)
            mainViewModel.onBottomNavSelected(TopLevelDestinations.HOME.id - 1)
            mainViewModel.getAds()
        }

        HomeScreen(
            goToFundWallet = goToFundWallet,
            onChatSupport = onChatSupport,
            loggedInUserState = loggedInUserState,
            onGenerateAccount = onGenerateAccount,
            onCopyAccountNumber = onCopyAccountNumber,
            onClickProduct = onClickProduct,
            banks = banksFlow,
            onSetTransactionPin = onSetTransactionPin,
            appUiState = appUiState,
            preferenceUiState = preferenceUiState,
            advertisements = ads,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}


fun NavController.navigateToHomeScreen(isAfterCheckout: Boolean = false) {
    navigate(AppNavigation.HomeScreen.route) {
        val backStack = if(isAfterCheckout) AppNavigation.HomeScreen.route else
            AppNavigation.AuthenticationScreen.route
        popUpTo(backStack) {
            inclusive = true
            saveState = false
        }
        launchSingleTop = true
        restoreState = true
    }
}