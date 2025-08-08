package com.example.miniecommerceapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


//defining colors
val PrimaryColor = androidx.compose.ui.graphics.Color(0xFF6200EE) // A deep purple
val PrimaryLightColor = androidx.compose.ui.graphics.Color(0xFFBB86FC)
val PrimaryDarkColor = androidx.compose.ui.graphics.Color(0xFF3700B3)
val SecondaryColor = androidx.compose.ui.graphics.Color(0xFF03DAC6) // A teal
val SecondaryLightColor = androidx.compose.ui.graphics.Color(0xFF66FFCC)
val SecondaryDarkColor = androidx.compose.ui.graphics.Color(0xFF018786)
val BackgroundColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
val SurfaceColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
val ErrorColor = androidx.compose.ui.graphics.Color(0xFFB00020)
val OnPrimaryColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
val OnSecondaryColor = androidx.compose.ui.graphics.Color(0xFF000000)
val OnBackgroundColor = androidx.compose.ui.graphics.Color(0xFF000000)
val OnSurfaceColor = androidx.compose.ui.graphics.Color(0xFF000000)
val OnErrorColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)



private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLightColor,
    onPrimary = OnPrimaryColor,
    primaryContainer = PrimaryDarkColor,
    onPrimaryContainer = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    secondaryContainer = SecondaryDarkColor,
    onSecondaryContainer = OnSecondaryColor,
    tertiary = PrimaryDarkColor, // Use a darker primary for tertiary in dark theme
    onTertiary = OnPrimaryColor,
    background = androidx.compose.ui.graphics.Color(0xFF121212), // Darker background
    onBackground = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    surface = androidx.compose.ui.graphics.Color(0xFF121212),
    onSurface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    error = ErrorColor,
    onError = OnErrorColor
)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    primaryContainer = PrimaryLightColor,
    onPrimaryContainer = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    secondaryContainer = SecondaryLightColor,
    onSecondaryContainer = OnSecondaryColor,
    tertiary = SecondaryColor, // Use secondary for tertiary in light theme
    onTertiary = OnSecondaryColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    error = ErrorColor,
    onError = OnErrorColor
)

@Composable
fun MiniEcommerceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}