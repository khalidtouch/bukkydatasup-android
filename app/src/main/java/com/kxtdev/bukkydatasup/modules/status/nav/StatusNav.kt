package com.kxtdev.bukkydatasup.modules.status.nav

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.navigateToHomeScreen
import com.kxtdev.bukkydatasup.modules.reset.nav.navigateToResetScreen
import com.kxtdev.bukkydatasup.modules.status.StatusScreen
import com.kxtdev.bukkydatasup.modules.transaction.nav.navigateToTransactionReceiptScreen
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionViewModel
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.poshComposable
import com.kxtdev.bukkydatasup.workers.RefreshWorker


fun NavGraphBuilder.statusScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    transactionViewModel: TransactionViewModel,
) {
    poshComposable(route = AppNavigation.StatusScreen.route) {
        val transactionStatus by mainViewModel.transactionStatus.collectAsStateWithLifecycle()
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()

        val context = LocalContext.current

        val onAction: () -> Unit = {
            when {
                appUiState.authState == AuthState.SIGNUP -> {
                    mainViewModel.updatePreferenceItem(PreferenceItem.RESET_TRANSACTION_PIN)
                    navController.navigateToResetScreen()
                }

                transactionStatus.canShowReceipt -> {

                    transactionStatus.itemId?.let { id ->
                        when(appUiState.product) {
                            Product.AIRTIME -> {
                                transactionViewModel.getAirtimeTransactionReceipt(id)
                            }
                            Product.DATA -> {
                                transactionViewModel.getDataTransactionReceipt(id)
                            }
                            Product.CABLE -> {
                                transactionViewModel.getCableReceipt(id)
                            }
                            Product.ELECTRICITY -> {
                                transactionViewModel.getBillReceipt(id)
                            }
                            Product.RESULT_CHECKER -> {
                                transactionViewModel.getResultCheckerReceipt(id)
                            }
                            else -> {
                                navController.navigateToHomeScreen(true)
                            }
                        }

                        navController.navigateToTransactionReceiptScreen()
                    }

                }

                else -> {
                    navController.navigateToHomeScreen(true)
                }
            }
        }

        LaunchedEffect(Unit) {
            RefreshWorker.engage(context)
        }

        BackHandler { onAction.invoke() }
        StatusScreen(
            header = transactionStatus.header,
            description = transactionStatus.description,
            status = transactionStatus.status,
            onAction = onAction,
            canShowReceipt = transactionStatus.canShowReceipt,
        )
    }
}

fun NavController.navigateToStatusScreen() {
    navigate(AppNavigation.StatusScreen.route)
}