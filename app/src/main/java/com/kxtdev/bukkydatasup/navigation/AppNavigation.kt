package com.kxtdev.bukkydatasup.navigation

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.kxtdev.bukkydatasup.common.utils.BiometricsPromptManager
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.authentication.nav.authenticationDetailScreen
import com.kxtdev.bukkydatasup.modules.authentication.nav.authenticationScreen
import com.kxtdev.bukkydatasup.modules.authentication.vm.AuthenticationViewModel
import com.kxtdev.bukkydatasup.modules.fund.nav.fundDetailScreen
import com.kxtdev.bukkydatasup.modules.fund.nav.fundScreen
import com.kxtdev.bukkydatasup.modules.fund.vm.FundViewModel
import com.kxtdev.bukkydatasup.modules.history.nav.historyDetailScreen
import com.kxtdev.bukkydatasup.modules.history.nav.historyScreen
import com.kxtdev.bukkydatasup.modules.history.vm.HistoryViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.homeScreen
import com.kxtdev.bukkydatasup.modules.preferences.nav.preferenceDetailScreen
import com.kxtdev.bukkydatasup.modules.preferences.nav.preferenceScreen
import com.kxtdev.bukkydatasup.modules.profile.nav.profileScreen
import com.kxtdev.bukkydatasup.modules.reset.nav.resetScreen
import com.kxtdev.bukkydatasup.modules.services.nav.serviceScreen
import com.kxtdev.bukkydatasup.modules.status.nav.statusScreen
import com.kxtdev.bukkydatasup.modules.transaction.nav.transactionCheckoutScreen
import com.kxtdev.bukkydatasup.modules.transaction.nav.transactionReceiptScreen
import com.kxtdev.bukkydatasup.modules.transaction.nav.transactionScreen
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionViewModel


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    exitApp: () -> Unit,
    clipboardManager: ClipboardManager,
    authenticationViewModel: AuthenticationViewModel,
    biometricsPromptManager: BiometricsPromptManager,
    applicationContext: Context,
    startDestination: String = AppNavigation.AuthenticationScreen.route,
) {
    val fundViewModel = hiltViewModel<FundViewModel>()
    val transactionViewModel = hiltViewModel<TransactionViewModel>()
    val historyViewModel = hiltViewModel<HistoryViewModel>()


    NavHost(
       navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
       authenticationScreen(
           navController = navController,
           mainViewModel = mainViewModel,
           exitApp = exitApp,
           authenticationViewModel = authenticationViewModel,
           biometricsPromptManager = biometricsPromptManager,
           applicationContext = applicationContext,
       )
        authenticationDetailScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            authenticationViewModel = authenticationViewModel,
        )
        homeScreen(
            mainViewModel = mainViewModel,
            navController = navController,
            clipboardManager = clipboardManager,
            transactionViewModel = transactionViewModel,
            fundViewModel = fundViewModel,
            onExitApp = exitApp,
        )
        fundScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            fundViewModel = fundViewModel,
            clipboardManager = clipboardManager,
        )
        fundDetailScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            fundViewModel = fundViewModel,
        )
        statusScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            transactionViewModel = transactionViewModel,
        )
        transactionScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            transactionViewModel = transactionViewModel,
            fundViewModel = fundViewModel,
            clipboardManager = clipboardManager,
        )
        transactionCheckoutScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            transactionViewModel = transactionViewModel,
            biometricsPromptManager = biometricsPromptManager,
            applicationContext = applicationContext,
        )
        transactionReceiptScreen(
            navController = navController,
            transactionViewModel = transactionViewModel,
            clipboardManager = clipboardManager,
        )
        historyScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            historyViewModel = historyViewModel,
        )
        historyDetailScreen(
            navController = navController,
            historyViewModel = historyViewModel,
            clipboardManager = clipboardManager,
        )
        profileScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            clipboardManager = clipboardManager,
            authenticationViewModel = authenticationViewModel,
        )
        preferenceScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            biometricsPromptManager = biometricsPromptManager,
            applicationContext = applicationContext,
        )
        preferenceDetailScreen(
            navController = navController,
            mainViewModel = mainViewModel,
        )
        resetScreen(
            navController = navController,
            mainViewModel = mainViewModel,
        )
        serviceScreen(
            navController = navController,
            mainViewModel = mainViewModel,
            transactionViewModel = transactionViewModel,
        )
    }
}


sealed class AppNavigation(val title: String, val route: String, val isTopLevel: Boolean) {
    data object AuthenticationScreen: AppNavigation("Authentication", "auth_screen", false)
    data object AuthenticationDetailScreen: AppNavigation("Authentication Details", "auth_detail_screen", false)

    data object HomeScreen: AppNavigation("Home", "home_screen", true)

    data object HistoryScreen: AppNavigation("History", "history_screen", true)
    data object HistoryDetailScreen: AppNavigation("History Details", "history_detail_screen", false) {
        const val recordId = "recordId"
        val routeWithArgs = "$route/{$recordId}"
        val arguments = listOf(navArgument(recordId) { type = NavType.IntType })
    }

    data object PreferenceScreen: AppNavigation("Preferences", "preference_screen", false)
    data object PreferenceDetailScreen: AppNavigation("Edit Preference", "preference_detail_screen", false)

    data object ProfileScreen: AppNavigation("Profile", "profile_screen", true)

    data object ResetScreen: AppNavigation("Reset", "reset_screen", false)

    data object TransactionScreen: AppNavigation("Transaction", "transaction_screen", false)
    data object TransactionCheckoutScreen: AppNavigation("Transaction Checkout", "transaction_checkout_screen", false)
    data object TransactionReceiptScreen: AppNavigation("Transaction Receipt", "transaction_receipt_screen", false)

    data object StatusScreen: AppNavigation("Status", "status_screen", false)
    data object PushNotificationScreen: AppNavigation("Announcements", "push_notification_screen", false)

    data object FundScreen: AppNavigation("Fund", "fund_screen", false)
    data object FundDetailScreen: AppNavigation("Fund Details", "fund_detail_screen", false)
    data object ServiceScreen: AppNavigation("Services", "service_screen", true)

}