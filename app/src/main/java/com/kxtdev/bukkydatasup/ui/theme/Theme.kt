package com.kxtdev.bukkydatasup.ui.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    primary = Blue50,
    onPrimary = White,
    primaryContainer = Blue50,
    onPrimaryContainer = White,
    secondary = Beige50,
    onSecondary = Blue99,
    secondaryContainer = Blue10,
    onSecondaryContainer = Blue90,
    tertiary = Gray95,
    onTertiary = Gray10,
    tertiaryContainer = Gray90,
    onTertiaryContainer = Gray10,
    error = Red50,
    onError = White,
    errorContainer = Red30,
    onErrorContainer = Red95,
    surface = Blue99,
    surfaceContainer = Blue95,
    surfaceBright = White,
    surfaceTint = Blue95,
    onSurface = Black,
    surfaceVariant = Blue90,
    onSurfaceVariant = Gray20,
    inverseSurface = Gray40,
    inverseOnSurface = Blue95,
    background = Blue99,
    onBackground = Gray10,
    outline = Gray50,
    outlineVariant = Gray70,
)

@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = Blue10,
    onPrimary = White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    secondary = Blue90,
    onSecondary = Blue99,
    secondaryContainer = Blue90,
    onSecondaryContainer = Blue30,
    tertiary = Gray40,
    onTertiary = White,
    tertiaryContainer = Gray30,
    onTertiaryContainer = Gray90,
    error = Red40,
    onError = White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    surface = Blue10,
    onSurface = White,
    surfaceVariant = Blue20,
    onSurfaceVariant = White,
    inverseSurface = Blue20,
    inverseOnSurface = White,
    background = Blue10,
    onBackground = White,
    outline = Gray99,
    surfaceContainer = Blue20,
    surfaceBright = Black,
    surfaceTint = Blue10,
    outlineVariant = Gray90,
)

@VisibleForTesting
val LightAndroidColorScheme = lightColorScheme(
    primary = Blue50,
    onPrimary = White,
    primaryContainer = Blue50,
    onPrimaryContainer = White,
    secondary = Beige50,
    onSecondary = Blue99,
    secondaryContainer = Blue10,
    onSecondaryContainer = Blue90,
    tertiary = Gray95,
    onTertiary = Gray10,
    tertiaryContainer = Gray90,
    onTertiaryContainer = Gray10,
    error = Red50,
    onError = White,
    errorContainer = Red30,
    onErrorContainer = Red95,
    surface = Blue99,
    surfaceContainer = Blue95,
    surfaceBright = White,
    surfaceTint = Blue95,
    onSurface = Black,
    surfaceVariant = Blue90,
    onSurfaceVariant = Gray20,
    inverseSurface = Gray40,
    inverseOnSurface = Blue95,
    background = Blue99,
    onBackground = Gray10,
    outline = Gray50,
    outlineVariant = Gray70,
)

@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
    primary = Blue10,
    onPrimary = White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    secondary = Blue90,
    onSecondary = Blue99,
    secondaryContainer = Blue90,
    onSecondaryContainer = Blue30,
    tertiary = Gray40,
    onTertiary = White,
    tertiaryContainer = Gray30,
    onTertiaryContainer = Gray90,
    error = Red40,
    onError = White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    surface = Blue10,
    onSurface = White,
    surfaceVariant = Blue20,
    onSurfaceVariant = White,
    inverseSurface = Blue20,
    inverseOnSurface = White,
    background = Blue10,
    onBackground = White,
    outline = Gray99,
    surfaceContainer = Blue20,
    surfaceBright = Black,
    surfaceTint = Blue10,
    outlineVariant = Gray90,
)


@Composable
fun MainAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = Blue10.toArgb()

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = !darkTheme
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = PoshTypography,
        content = content
    )
}


@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

val Transparent: Color = Color.Transparent