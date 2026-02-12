package com.kxtdev.bukkydatasup.modules.fund.nav

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
import com.kxtdev.bukkydatasup.common.enums.FundOption
import com.kxtdev.bukkydatasup.common.utils.appToast
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.fund.FundDetailScreen
import com.kxtdev.bukkydatasup.modules.fund.FundScreen
import com.kxtdev.bukkydatasup.modules.fund.vm.FundViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.navigateToHomeScreen
import com.kxtdev.bukkydatasup.modules.status.nav.navigateToStatusScreen
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.poshComposable

fun NavGraphBuilder.fundScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    fundViewModel: FundViewModel,
    clipboardManager: ClipboardManager,
) {
    poshComposable(route = AppNavigation.FundScreen.route) {
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()
        val banksFlow by fundViewModel.banksFlowCached.collectAsStateWithLifecycle()

        val context = LocalContext.current

        val onBackPressed: () -> Unit = {
            navController.navigateToHomeScreen()
        }

        val onGenerateAccount: () -> Unit = {

        }

        val onCopyAccountNumber: (String) -> Unit = { data ->
            val accountNumberData = ClipData.newPlainText("accountNumber", data)
            clipboardManager.setPrimaryClip(accountNumberData)
            appToast(context, context.getString(R.string.copied))
        }

        val onClickFundOption: (FundOption) -> Unit = { option ->
            mainViewModel.updateFundOption(option)
            fundViewModel.updateFundOption(option)
            navController.navigateToFundDetailScreen()
        }


        FundScreen(
            onBackPressed = onBackPressed,
            onGenerateAccount = onGenerateAccount,
            onCopyAccountNumber = onCopyAccountNumber,
            onClickFundOption =  onClickFundOption,
            banks = banksFlow,
            appUiState = appUiState,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}

fun NavGraphBuilder.fundDetailScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    fundViewModel: FundViewModel,
) {
    poshComposable(route = AppNavigation.FundDetailScreen.route) {
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()
        val fundingRequest by fundViewModel.fundingRequest.collectAsStateWithLifecycle()
        val fundingUiState by fundViewModel.fundingUiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        val onBackPressed: () -> Unit = {
            navController.navigateToFundScreen()
        }

        val onAction: () -> Unit = {
            when(appUiState.selectedFundOption) {
                FundOption.FUND_WITH_COUPON -> {
                    fundViewModel.fundWithCoupon(mainViewModel::updateTransactionStatus)
                }
                FundOption.FUND_WITH_MONNIFY_CARD -> {
                    fundViewModel.fundWithMonnify(context)
                }
                FundOption.FUND_FROM_BONUS -> {
                    fundViewModel.withdrawFromReferralBonus(mainViewModel::updateTransactionStatus)
                }
                else -> Unit
            }
        }

        val updateValue: (String) -> Unit = { v ->
            fundViewModel.updateValue(v, fundingRequest.fundOption)
        }

        val onHandleThrowable: () -> Unit = {
            fundViewModel.onHandledThrowable()
        }

        LaunchedEffect(fundingUiState.shouldShowStatusPage) {
            if(fundingUiState.shouldShowStatusPage == true) {
                navController.navigateToStatusScreen()
                fundViewModel.updateShowStatusPage(null)
            }
        }


        FundDetailScreen(
            onBackPressed = onBackPressed,
            fundingRequest = fundingRequest,
            fundingUiState = fundingUiState,
            appUiState = appUiState,
            onAction = onAction,
            updateValue = updateValue,
            onHandleThrowable = onHandleThrowable,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}

fun NavController.navigateToFundScreen() {
    navigate(AppNavigation.FundScreen.route)
}

fun NavController.navigateToFundDetailScreen() {
    navigate(AppNavigation.FundDetailScreen.route)
}
