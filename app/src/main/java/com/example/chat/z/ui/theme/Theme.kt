package com.example.chat.z.ui.theme

import android.app.Activity
import android.os.Build
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

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = Color(0xFFE3F2FD),

    secondary = SecondaryAmber,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = SecondaryAmberDark,
    onSecondaryContainer = Color(0xFFFFE0B2),

    tertiary = AccentPurple,
    onTertiary = Color(0xFFFFFFFF),

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,

    error = AccentRed,
    onError = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF01579B),

    secondary = SecondaryAmber,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = SecondaryAmberLight,
    onSecondaryContainer = Color(0xFFE65100),

    tertiary = AccentPurple,
    onTertiary = Color(0xFFFFFFFF),

    background = BackgroundLight,
    onBackground = TextPrimaryLight,

    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,

    surfaceContainer = Color(0xFFF5F5F5),
    surfaceContainerHigh = Color(0xFFEEEEEE),
    surfaceContainerHighest = Color(0xFFE0E0E0),

    error = AccentRed,
    onError = Color(0xFFFFFFFF)
)

@Composable
fun ChatZTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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

    // Smooth animation spec for all color transitions
    val colorAnimationSpec = androidx.compose.animation.core.tween<androidx.compose.ui.graphics.Color>(
        durationMillis = 600,
        easing = androidx.compose.animation.core.FastOutSlowInEasing
    )

    // Animate ALL colors for smooth theme transitions
    val animatedColorScheme = androidx.compose.material3.ColorScheme(
        primary = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.primary,
            animationSpec = colorAnimationSpec,
            label = "primary"
        ).value,
        onPrimary = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onPrimary,
            animationSpec = colorAnimationSpec,
            label = "onPrimary"
        ).value,
        primaryContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.primaryContainer,
            animationSpec = colorAnimationSpec,
            label = "primaryContainer"
        ).value,
        onPrimaryContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onPrimaryContainer,
            animationSpec = colorAnimationSpec,
            label = "onPrimaryContainer"
        ).value,
        secondary = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.secondary,
            animationSpec = colorAnimationSpec,
            label = "secondary"
        ).value,
        onSecondary = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onSecondary,
            animationSpec = colorAnimationSpec,
            label = "onSecondary"
        ).value,
        secondaryContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.secondaryContainer,
            animationSpec = colorAnimationSpec,
            label = "secondaryContainer"
        ).value,
        onSecondaryContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onSecondaryContainer,
            animationSpec = colorAnimationSpec,
            label = "onSecondaryContainer"
        ).value,
        tertiary = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.tertiary,
            animationSpec = colorAnimationSpec,
            label = "tertiary"
        ).value,
        onTertiary = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onTertiary,
            animationSpec = colorAnimationSpec,
            label = "onTertiary"
        ).value,
        tertiaryContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.tertiaryContainer,
            animationSpec = colorAnimationSpec,
            label = "tertiaryContainer"
        ).value,
        onTertiaryContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onTertiaryContainer,
            animationSpec = colorAnimationSpec,
            label = "onTertiaryContainer"
        ).value,
        error = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.error,
            animationSpec = colorAnimationSpec,
            label = "error"
        ).value,
        onError = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onError,
            animationSpec = colorAnimationSpec,
            label = "onError"
        ).value,
        errorContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.errorContainer,
            animationSpec = colorAnimationSpec,
            label = "errorContainer"
        ).value,
        onErrorContainer = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onErrorContainer,
            animationSpec = colorAnimationSpec,
            label = "onErrorContainer"
        ).value,
        background = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.background,
            animationSpec = colorAnimationSpec,
            label = "background"
        ).value,
        onBackground = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onBackground,
            animationSpec = colorAnimationSpec,
            label = "onBackground"
        ).value,
        surface = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.surface,
            animationSpec = colorAnimationSpec,
            label = "surface"
        ).value,
        onSurface = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onSurface,
            animationSpec = colorAnimationSpec,
            label = "onSurface"
        ).value,
        surfaceVariant = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.surfaceVariant,
            animationSpec = colorAnimationSpec,
            label = "surfaceVariant"
        ).value,
        onSurfaceVariant = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.onSurfaceVariant,
            animationSpec = colorAnimationSpec,
            label = "onSurfaceVariant"
        ).value,
        outline = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.outline,
            animationSpec = colorAnimationSpec,
            label = "outline"
        ).value,
        outlineVariant = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.outlineVariant,
            animationSpec = colorAnimationSpec,
            label = "outlineVariant"
        ).value,
        scrim = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.scrim,
            animationSpec = colorAnimationSpec,
            label = "scrim"
        ).value,
        inverseSurface = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.inverseSurface,
            animationSpec = colorAnimationSpec,
            label = "inverseSurface"
        ).value,
        inverseOnSurface = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.inverseOnSurface,
            animationSpec = colorAnimationSpec,
            label = "inverseOnSurface"
        ).value,
        inversePrimary = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.inversePrimary,
            animationSpec = colorAnimationSpec,
            label = "inversePrimary"
        ).value,
        surfaceTint = androidx.compose.animation.animateColorAsState(
            targetValue = colorScheme.surfaceTint,
            animationSpec = colorAnimationSpec,
            label = "surfaceTint"
        ).value
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = animatedColorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = animatedColorScheme,
        content = content
    )
}