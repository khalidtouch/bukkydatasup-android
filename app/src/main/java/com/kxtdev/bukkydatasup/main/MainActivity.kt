package com.kxtdev.bukkydatasup.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kxtdev.bukkydatasup.app.MainApp
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.AppNavigationState
import com.kxtdev.bukkydatasup.navigation.TopLevelDestinations
import com.kxtdev.bukkydatasup.navigation.rememberAppNavigationState
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var appNavigationState: AppNavigationState? = null
    private var mainViewModel: MainViewModel? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            mainViewModel = hiltViewModel<MainViewModel>()
            appNavigationState = rememberAppNavigationState()
            val preferenceUiState by mainViewModel!!.preferenceUiState.collectAsStateWithLifecycle()

            val systemUiController = rememberSystemUiController()
            val darkTheme = MainTheme.shouldUseDarkTheme(preferenceUiState = preferenceUiState)
            val shouldUseAndroidTheme = MainTheme.shouldUseAndroidTheme(preferenceUiState = preferenceUiState)
            val shouldDisableDynamicTheming = MainTheme.shouldDisableDynamicTheming(
                preferenceUiState = preferenceUiState
            )

            DisposableEffect(Unit) {
                requestPushNotificationPermission()
                onDispose {  }
            }

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {  }
            }

            MainAppTheme (
                darkTheme = false,
                androidTheme = shouldUseAndroidTheme,
                disableDynamicTheming = shouldDisableDynamicTheming
            ){
                MainApp(
                    mainViewModel = mainViewModel!!,
                    navController = appNavigationState?.navController!!,
                    currentDestination = appNavigationState?.currentDestination!!,
                    parentActivity = this,
                    exitApp = { this.finish() },
                    onSelectBottomNav = { index ->
                        mainViewModel!!.onBottomNavSelected(index)
                        TopLevelDestinations.entries.find { it.id == index + 1 }?.let { destination ->
                            appNavigationState?.navController!!.navigate(destination.route) {
                                if(destination.route == AppNavigation.HomeScreen.route) {
                                    popUpTo(AppNavigation.AuthenticationScreen.route) {
                                        inclusive = true
                                        saveState = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    private fun requestPushNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {

            } else if(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}