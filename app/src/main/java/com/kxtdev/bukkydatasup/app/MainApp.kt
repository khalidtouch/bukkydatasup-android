package com.kxtdev.bukkydatasup.app

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.utils.BiometricsPromptManager
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.authentication.nav.navigateToAuthenticationScreen
import com.kxtdev.bukkydatasup.modules.authentication.vm.AuthenticationViewModel
import com.kxtdev.bukkydatasup.navigation.AppNavHost
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.ui.design.PoshCustomBackground
import com.kxtdev.bukkydatasup.ui.design.PoshNavigationBar
import com.kxtdev.bukkydatasup.workers.RefreshWorker


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    currentDestination: AppNavigation,
    onSelectBottomNav: (index: Int) -> Unit,
    exitApp: () -> Unit,
    parentActivity: FragmentActivity,
) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()
    val timerState by mainViewModel.timerState.collectAsStateWithLifecycle()
    val preferenceUiState by mainViewModel.preferenceUiState.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE)
            as ClipboardManager
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer

    val onRefresh: () -> Unit = {
        mainViewModel.getAlertNotification()
        mainViewModel.getDisabledServices()
        mainViewModel.getDisabledNetworks()
        mainViewModel.refreshUser()
        mainViewModel.getAds()
        RefreshWorker.engage(context)
    }

    val promptManager by lazy {
        BiometricsPromptManager(parentActivity)
    }
    val applicationContext = parentActivity.applicationContext

    LaunchedEffect(timerState.isTimedOut) {
        if(timerState.isTimedOut == true) {
            mainViewModel.updateAuthState(
                if (preferenceUiState.shouldEnablePassCode)
                    AuthState.QUICK_SIGN_IN else
                    AuthState.LOGIN
            )
            authenticationViewModel.updateAuthState(
                if (preferenceUiState.shouldEnablePassCode)
                    AuthState.QUICK_SIGN_IN else
                    AuthState.LOGIN
            )
            navController.navigateToAuthenticationScreen()
        }
    }

    PullToRefreshBox(
        state = pullRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh.invoke() },
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = containerColor,
                color = contentColor,
                state = pullRefreshState
            )
        }
    )  {

        PoshCustomBackground(
            Modifier
                .fillMaxSize(),
            primaryColor = MaterialTheme.colorScheme.primary,
        ) {
            AppNavHost(
                modifier = modifier,
                mainViewModel = mainViewModel,
                navController = navController,
                exitApp = exitApp,
                clipboardManager = clipboardManager,
                authenticationViewModel = authenticationViewModel,
                biometricsPromptManager = promptManager,
                applicationContext = applicationContext,
            )
            if (currentDestination.isTopLevel) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    PoshNavigationBar(
                        selectedItemIndex = appUiState.selectedBottomNavItemIndex,
                        onClick = onSelectBottomNav
                    )
                }
            }

        }
    }

}