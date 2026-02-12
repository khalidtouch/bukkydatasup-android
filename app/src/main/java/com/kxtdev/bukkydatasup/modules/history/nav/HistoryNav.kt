package com.kxtdev.bukkydatasup.modules.history.nav

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
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.models.HistoryListItem
import com.kxtdev.bukkydatasup.common.utils.appToast
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.history.HistoryDetailScreen
import com.kxtdev.bukkydatasup.modules.history.HistoryScreen
import com.kxtdev.bukkydatasup.modules.history.vm.HistoryViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.navigateToHomeScreen
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.TopLevelDestinations
import com.kxtdev.bukkydatasup.navigation.poshComposable

fun NavGraphBuilder.historyScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    historyViewModel: HistoryViewModel,
) {
    poshComposable(route = AppNavigation.HistoryScreen.route) {
        val historyUiState by historyViewModel.historyUiState.collectAsStateWithLifecycle()
        val currentPage by historyViewModel.currentPage.collectAsStateWithLifecycle()
        val airtimeHistoryCached by historyViewModel.airtimeHistoryCached.collectAsStateWithLifecycle()
        val airtimeHistoryPageCount by historyViewModel.airtimeHistoryRecordPageCount.collectAsStateWithLifecycle()
        val dataHistoryCached by historyViewModel.dataHistoryCached.collectAsStateWithLifecycle()
        val dataHistoryPageCount by historyViewModel.dataHistoryRecordPageCount.collectAsStateWithLifecycle()
        val cableHistoryCached by historyViewModel.cableHistoryCached.collectAsStateWithLifecycle()
        val cableHistoryPageCount by historyViewModel.cableHistoryRecordPageCount.collectAsStateWithLifecycle()
        val meterHistoryCached by historyViewModel.meterHistoryCached.collectAsStateWithLifecycle()
        val meterHistoryPageCount by historyViewModel.meterHistoryRecordPageCount.collectAsStateWithLifecycle()
        val resultCheckerHistoryCached by historyViewModel.resultCheckerHistoryCached.collectAsStateWithLifecycle()
        val resultCheckerHistoryPageCount by historyViewModel.resultCheckerHistoryRecordPageCount.collectAsStateWithLifecycle()
        val walletSummaryHistoryCached by historyViewModel.walletSummaryHistoryCached.collectAsStateWithLifecycle()
        val walletSummaryHistoryPageCount by historyViewModel.walletSummaryRecordPageCount.collectAsStateWithLifecycle()
        val printCardHistoryCached by historyViewModel.printCardHistoryCached.collectAsStateWithLifecycle()
        val printCardHistoryPageCount by historyViewModel.printCardRecordPageCount.collectAsStateWithLifecycle()
        val bulkSMSHistoryCached by historyViewModel.bulkSMSHistoryCached.collectAsStateWithLifecycle()
        val bulkSMSHistoryPageCount by historyViewModel.bulkSMSRecordPageCount.collectAsStateWithLifecycle()


        val onToggleFilterExpansion: (Boolean) -> Unit = { state ->
            historyViewModel.updateFilterExpansionState(state)
        }


        val onSelectHistoryItem: (String?) -> Unit = { stringedItem ->
            val product = Product.getByTitle(stringedItem.orEmpty())
            historyViewModel.selectHistoryProduct(product)
        }

        val loadDetail: (id: Int) -> Unit = { itemId ->
            when(historyUiState.selectedProduct) {
                Product.AIRTIME -> {
                    historyViewModel.getAirtimeHistoryDetailItem(itemId)
                }
                Product.DATA -> {
                    historyViewModel.getDataHistoryDetailItem(itemId)
                }
                Product.CABLE -> {
                    historyViewModel.getCableHistoryDetailItem(itemId)
                }
                Product.RESULT_CHECKER -> {
                    historyViewModel.getResultCheckerHistoryDetailItem(itemId)
                }
                Product.ELECTRICITY -> {
                    historyViewModel.getMeterHistoryDetailItem(itemId)
                }
                Product.WALLET_HISTORY -> {
                    historyViewModel.getWalletSummaryHistoryDetailItem(itemId)
                }
                Product.PRINT_CARD -> {
                    historyViewModel.getPrintCardHistoryDetailItem(itemId)
                }
                Product.BULK_SMS -> {
                    historyViewModel.getBulkSMSHistoryDetailItem(itemId)
                }

                else -> Unit
            }

        }

        val onViewDetails: (Int) -> Unit = { id ->
            loadDetail.invoke(id)
            navController.navigateToHistoryDetailScreen(id)
        }

        LaunchedEffect(Unit) {
            mainViewModel.onBottomNavSelected(TopLevelDestinations.HISTORY.id - 1)
        }

        LaunchedEffect(historyUiState.selectedProduct) {
            historyViewModel.resetPage()
        }

        val onPrev: () -> Unit = {
            historyViewModel.onPrev(currentPage)
        }

        val onNext: () -> Unit = {
            historyViewModel.onNext(currentPage)
        }

        val onBackPressed: () -> Unit = {
            navController.navigateToHomeScreen()
        }

        val historyListItems: List<HistoryListItem>? = when(historyUiState.selectedProduct) {
            Product.AIRTIME -> airtimeHistoryCached
            Product.DATA -> dataHistoryCached
            Product.CABLE -> cableHistoryCached
            Product.ELECTRICITY -> meterHistoryCached
            Product.RESULT_CHECKER -> resultCheckerHistoryCached
            Product.WALLET_HISTORY -> walletSummaryHistoryCached
            Product.PRINT_CARD -> printCardHistoryCached
            Product.BULK_SMS -> bulkSMSHistoryCached
            else -> null
        }

        val historyListPageCount: Long? = when (historyUiState.selectedProduct) {
            Product.AIRTIME -> airtimeHistoryPageCount
            Product.DATA -> dataHistoryPageCount
            Product.CABLE -> cableHistoryPageCount
            Product.ELECTRICITY -> meterHistoryPageCount
            Product.RESULT_CHECKER -> resultCheckerHistoryPageCount
            Product.WALLET_HISTORY -> walletSummaryHistoryPageCount
            Product.PRINT_CARD -> printCardHistoryPageCount
            Product.BULK_SMS -> bulkSMSHistoryPageCount
            else -> null
        }

        HistoryScreen(
            historyUiState = historyUiState,
            onToggleFilterExpansion = onToggleFilterExpansion,
            onSelectHistoryItem = onSelectHistoryItem,
            onViewDetails = onViewDetails,
            onPrev = onPrev,
            onNext = onNext,
            currentPage = currentPage,
            historyListItems = historyListItems,
            historyListPageCount = historyListPageCount,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}


fun NavGraphBuilder.historyDetailScreen(
    navController: NavHostController,
    historyViewModel: HistoryViewModel,
    clipboardManager: ClipboardManager,
) {
    poshComposable(
        route = AppNavigation.HistoryDetailScreen.routeWithArgs,
        arguments = AppNavigation.HistoryDetailScreen.arguments
    ) { entry ->
        val recordId = entry.arguments?.getInt(AppNavigation.HistoryDetailScreen.recordId)
        val historyUiState by historyViewModel.historyUiState.collectAsStateWithLifecycle()

        val context = LocalContext.current

        val onBackPressed: () -> Unit = {
            navController.navigateToHistoryScreen()
        }

        val onCopy: (String) -> Unit = { data ->
            val accountNumberData = ClipData.newPlainText("apiResponse", data)
            clipboardManager.setPrimaryClip(accountNumberData)
            appToast(context, context.getString(R.string.copied))
        }

        val onHandleThrowable: () -> Unit = {
            historyViewModel.onHandledThrowable()
        }

        recordId?.let {

            HistoryDetailScreen(
                historyUiState = historyUiState,
                onCopy = onCopy,
                onBackPressed = onBackPressed,
                onHandleThrowable = onHandleThrowable,
            )
        }

        BackHandler {
            onBackPressed.invoke()
        }
    }
}



fun NavController.navigateToHistoryScreen() {
    navigate(AppNavigation.HistoryScreen.route)
}

fun NavController.navigateToHistoryDetailScreen(id: Int) {
    navigate("${AppNavigation.HistoryDetailScreen.route}/$id")
}