package com.kxtdev.bukkydatasup.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavGraphBuilder.poshComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList<NavDeepLink>(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { fadeEnter() },
        exitTransition = { fadeExit() },
        popEnterTransition = { fadePopEnter() },
        popExitTransition = { fadePopExit() },
        deepLinks = deepLinks,
        content = content,
    )
}

private fun AnimatedContentTransitionScope<*>.fadeEnter(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 320,
            easing = FastOutSlowInEasing
        )
    )
}

private fun AnimatedContentTransitionScope<*>.fadeExit(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )
}

private fun AnimatedContentTransitionScope<*>.fadePopEnter(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        )
    )
}

private fun AnimatedContentTransitionScope<*>.fadePopExit(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = 180,
            easing = LinearOutSlowInEasing
        )
    )
}



fun NavController.jumpBackTo(route: String) {
    navigate(route) {
        popUpTo(AppNavigation.AuthenticationScreen.route) {
            inclusive = true
            saveState = false
        }
//        launchSingleTop = true
//        restoreState = true
    }
}