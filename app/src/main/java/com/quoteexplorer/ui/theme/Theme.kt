package com.quoteexplorer.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light color scheme matching the warm yellow design
private val QuoteExplorerColorScheme = lightColorScheme(
    primary = PrimaryYellow,
    onPrimary = DarkCharcoal,
    primaryContainer = AccentGold,
    onPrimaryContainer = DarkCharcoal,
    
    secondary = DarkCharcoal,
    onSecondary = SurfaceWhite,
    secondaryContainer = DarkCharcoal,
    onSecondaryContainer = SurfaceWhite,
    
    tertiary = AccentGold,
    onTertiary = DarkCharcoal,
    
    background = Background,
    onBackground = DarkCharcoal,
    
    surface = SurfaceWhite,
    onSurface = DarkCharcoal,
    surfaceVariant = Background,
    onSurfaceVariant = SoftGray,
    
    error = ErrorRed,
    onError = SurfaceWhite,
    
    outline = LightGray,
    outlineVariant = LightGray
)

@Composable
fun QuoteExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For now, we only support light theme to match the design
    val colorScheme = QuoteExplorerColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuoteExplorerTypography,
        shapes = QuoteExplorerShapes,
        content = content
    )
}
