package com.kxtdev.bukkydatasup.modules.profile.nav

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
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.utils.appToast
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.authentication.nav.navigateToAuthenticationScreen
import com.kxtdev.bukkydatasup.modules.authentication.vm.AuthenticationViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.navigateToHomeScreen
import com.kxtdev.bukkydatasup.modules.preferences.nav.navigateToPreferenceDetailScreen
import com.kxtdev.bukkydatasup.modules.preferences.nav.navigateToPreferenceScreen
import com.kxtdev.bukkydatasup.modules.profile.ProfileScreen
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.TopLevelDestinations
import com.kxtdev.bukkydatasup.navigation.poshComposable

fun NavGraphBuilder.profileScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    clipboardManager: ClipboardManager,
    authenticationViewModel: AuthenticationViewModel,
) {
    poshComposable(route = AppNavigation.ProfileScreen.route) {
        val context = LocalContext.current
        val loggedInUserState by mainViewModel.loggedInUserState.collectAsStateWithLifecycle()

        val onCopy: (String) -> Unit = { data ->
            val accountNumberData = ClipData.newPlainText("profileItem", data)
            clipboardManager.setPrimaryClip(accountNumberData)
            appToast(context, context.getString(R.string.copied))
        }

        val goToPreferences: () -> Unit = {
            navController.navigateToPreferenceScreen()
        }

        val onUpgradeAccount: () -> Unit = {
            mainViewModel.updatePreferenceItem(PreferenceItem.UPGRADE_ACCOUNT)
            navController.navigateToPreferenceDetailScreen()
        }

        val onVerifyAccount: () -> Unit = {
            mainViewModel.updatePreferenceItem(PreferenceItem.VERIFY_ACCOUNT)
            navController.navigateToPreferenceDetailScreen()
        }

        val onLogout: () -> Unit = {
            mainViewModel.logout(context) {
                mainViewModel.updateAuthState(AuthState.WELCOME)
                authenticationViewModel.updateAuthState(AuthState.WELCOME)
                navController.navigateToAuthenticationScreen()
            }
        }

        val onBackPressed: () -> Unit = {
            navController.navigateToHomeScreen()
        }

        LaunchedEffect(Unit) {
            mainViewModel.onBottomNavSelected(TopLevelDestinations.PROFILE.id - 1)
        }

        ProfileScreen(
            onCopy = onCopy,
            goToPreferences = goToPreferences,
            onVerifyAccount = onVerifyAccount,
            onLogout = onLogout,
            loggedInUserState = loggedInUserState,
            onUpgradeAccount = onUpgradeAccount,
        )

        BackHandler {
            onBackPressed.invoke()
        }

    }
}


fun NavController.navigateToProfileScreen() {
    navigate(AppNavigation.ProfileScreen.route)
}
