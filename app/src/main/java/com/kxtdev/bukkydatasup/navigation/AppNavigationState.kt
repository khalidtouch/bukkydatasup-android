package com.kxtdev.bukkydatasup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class AppNavigationState(
    val navController: NavHostController
) {

    private val currentRoute: String?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route

    val currentDestination: AppNavigation
        @Composable get() = AppNavigationFactory.getDestination(currentRoute)

    object AppNavigationFactory {
        fun getDestination(route: String?): AppNavigation {
            return when (route) {
                AppNavigation.AuthenticationScreen.route -> AppNavigation.AuthenticationScreen
                AppNavigation.AuthenticationDetailScreen.route -> AppNavigation.AuthenticationDetailScreen
                AppNavigation.HomeScreen.route -> AppNavigation.HomeScreen
                AppNavigation.HistoryScreen.route -> AppNavigation.HistoryScreen
                AppNavigation.HistoryDetailScreen.route -> AppNavigation.HistoryDetailScreen
                AppNavigation.PreferenceScreen.route -> AppNavigation.PreferenceScreen
                AppNavigation.PreferenceDetailScreen.route -> AppNavigation.PreferenceDetailScreen
                AppNavigation.ProfileScreen.route -> AppNavigation.ProfileScreen
                AppNavigation.ResetScreen.route -> AppNavigation.ResetScreen
                AppNavigation.TransactionScreen.route -> AppNavigation.TransactionScreen
                AppNavigation.TransactionCheckoutScreen.route -> AppNavigation.TransactionCheckoutScreen
                AppNavigation.TransactionReceiptScreen.route -> AppNavigation.TransactionReceiptScreen
                AppNavigation.StatusScreen.route -> AppNavigation.StatusScreen
                AppNavigation.PushNotificationScreen.route -> AppNavigation.PushNotificationScreen
                AppNavigation.FundScreen.route -> AppNavigation.FundScreen
                AppNavigation.FundDetailScreen.route -> AppNavigation.FundDetailScreen
                AppNavigation.ServiceScreen.route -> AppNavigation.ServiceScreen
                else -> AppNavigation.AuthenticationScreen
            }
        }
    }
}

@Composable
fun rememberAppNavigationState(
    navController: NavHostController = rememberNavController()
): AppNavigationState = remember(navController) {
    AppNavigationState(navController)
}