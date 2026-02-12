package com.kxtdev.bukkydatasup.navigation

import com.kxtdev.bukkydatasup.ui.design.PoshIcon

enum class TopLevelDestinations(val id: Int, val title: String, val route: String, val icon: Int) {
    HOME(1, "Home", AppNavigation.HomeScreen.route, PoshIcon.Home),
    SERVICES(2, "Services", AppNavigation.ServiceScreen.route, PoshIcon.Services),
    HISTORY(3, "History", AppNavigation.HistoryScreen.route, PoshIcon.Time),
    PROFILE(4, "Profile", AppNavigation.ProfileScreen.route, PoshIcon.Avatar);
}