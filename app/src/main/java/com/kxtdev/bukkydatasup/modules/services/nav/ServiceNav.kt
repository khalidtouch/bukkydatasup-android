package com.kxtdev.bukkydatasup.modules.services.nav

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.navigateToHomeScreen
import com.kxtdev.bukkydatasup.modules.services.ServiceScreen
import com.kxtdev.bukkydatasup.modules.transaction.nav.navigateToTransactionScreen
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionViewModel
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.TopLevelDestinations
import com.kxtdev.bukkydatasup.navigation.poshComposable

fun NavGraphBuilder.serviceScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    transactionViewModel: TransactionViewModel,
) {
    poshComposable(route = AppNavigation.ServiceScreen.route) {

        LaunchedEffect(Unit) {
            mainViewModel.onBottomNavSelected(TopLevelDestinations.SERVICES.id - 1)
        }

        val onNavigateToTransactionScreen: (Product) -> Unit = { product ->
            mainViewModel.selectProduct(product)
            transactionViewModel.updateTransactionProduct(product)
            transactionViewModel.updateFromDestination(AppNavigation.ServiceScreen.route)
            navController.navigateToTransactionScreen()
        }

        val onBackPressed: () -> Unit = {
            navController.navigateToHomeScreen()
        }

        ServiceScreen(
            onNavigateToTransactionScreen = onNavigateToTransactionScreen
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}

fun NavController.navigateToServiceScreen() {
    navigate(AppNavigation.ServiceScreen.route) {
        popUpTo(AppNavigation.HomeScreen.route) {
            inclusive = true
            saveState = false
        }
        launchSingleTop = true
        restoreState = true
    }
}