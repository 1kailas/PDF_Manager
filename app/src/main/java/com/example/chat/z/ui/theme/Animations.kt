package com.example.chat.z.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/**
 * Professional animation specifications optimized for 120Hz displays
 * Following Material Design 3 motion principles with enhanced fluidity
 */

// Duration constants optimized for high refresh rate displays
object AnimationDurations {
    const val INSTANT = 50
    const val QUICK = 100
    const val FAST = 200
    const val MEDIUM = 300
    const val SMOOTH = 400
    const val SLOW = 500
}

// Professional easing curves for different use cases
object AnimationEasing {
    // Emphasized - For important transitions (screen changes, major state changes)
    val Emphasized = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)

    // Standard - For most UI animations
    val Standard = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val StandardDecelerate = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
    val StandardAccelerate = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    // Legacy - For backwards compatibility
    val Legacy = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
}

// Spring configurations for fluid, natural motion
object SpringConfig {
    // High stiffness for immediate feedback
    val Bouncy = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )

    // Medium stiffness for balanced motion
    val Smooth = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Low stiffness for gentle motion
    val Gentle = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Very low stiffness for slow, dramatic motion
    val Dramatic = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow
    )
}

// Pre-configured animation specs for common use cases
object AnimationSpecs {
    // For screen transitions and major layout changes
    fun <T> screenTransition() = tween<T>(
        durationMillis = AnimationDurations.SMOOTH,
        easing = AnimationEasing.EmphasizedDecelerate
    )

    // For element entrance animations
    fun <T> elementEnter() = tween<T>(
        durationMillis = AnimationDurations.MEDIUM,
        easing = AnimationEasing.EmphasizedDecelerate
    )

    // For element exit animations
    fun <T> elementExit() = tween<T>(
        durationMillis = AnimationDurations.FAST,
        easing = AnimationEasing.EmphasizedAccelerate
    )

    // For interactive element state changes (buttons, switches)
    fun <T> interaction() = tween<T>(
        durationMillis = AnimationDurations.FAST,
        easing = AnimationEasing.Standard
    )

    // For smooth value changes (progress, sliders)
    fun <T> valueChange() = tween<T>(
        durationMillis = AnimationDurations.MEDIUM,
        easing = AnimationEasing.StandardDecelerate
    )

    // For quick feedback (ripples, highlights)
    fun <T> feedback() = tween<T>(
        durationMillis = AnimationDurations.QUICK,
        easing = AnimationEasing.StandardAccelerate
    )

    // Spring-based animations for natural feel
    fun <T> springBouncy() = spring<T>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )

    fun <T> springSmooth() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
}

// Content transition specs for AnimatedContent
object ContentTransitions {
    // Fade through - Material Design 3 standard
    fun fadeThrough(
        duration: Int = AnimationDurations.MEDIUM
    ) = fadeIn(
        animationSpec = tween(duration, easing = AnimationEasing.StandardDecelerate)
    ) togetherWith fadeOut(
        animationSpec = tween(duration / 2, easing = AnimationEasing.StandardAccelerate)
    )

    // Shared axis X - For horizontal navigation
    fun sharedAxisX(
        forward: Boolean = true,
        duration: Int = AnimationDurations.SMOOTH
    ): ContentTransform {
        val slideDistance = if (forward) 50 else -50
        return (slideInHorizontally(
            animationSpec = tween(duration, easing = AnimationEasing.EmphasizedDecelerate),
            initialOffsetX = { slideDistance }
        ) + fadeIn(
            animationSpec = tween(duration, easing = AnimationEasing.StandardDecelerate),
            initialAlpha = 0f
        )) togetherWith (slideOutHorizontally(
            animationSpec = tween(duration, easing = AnimationEasing.EmphasizedAccelerate),
            targetOffsetX = { -slideDistance }
        ) + fadeOut(
            animationSpec = tween(duration / 2, easing = AnimationEasing.StandardAccelerate),
            targetAlpha = 0f
        ))
    }

    // Shared axis Y - For vertical navigation
    fun sharedAxisY(
        forward: Boolean = true,
        duration: Int = AnimationDurations.SMOOTH
    ): ContentTransform {
        val slideDistance = if (forward) 50 else -50
        return (slideInVertically(
            animationSpec = tween(duration, easing = AnimationEasing.EmphasizedDecelerate),
            initialOffsetY = { slideDistance }
        ) + fadeIn(
            animationSpec = tween(duration, easing = AnimationEasing.StandardDecelerate),
            initialAlpha = 0f
        )) togetherWith (slideOutVertically(
            animationSpec = tween(duration, easing = AnimationEasing.EmphasizedAccelerate),
            targetOffsetY = { -slideDistance }
        ) + fadeOut(
            animationSpec = tween(duration / 2, easing = AnimationEasing.StandardAccelerate),
            targetAlpha = 0f
        ))
    }

    // Scale - For expanding/collapsing content
    fun scaleTransform(
        duration: Int = AnimationDurations.MEDIUM
    ) = scaleIn(
        animationSpec = tween(duration, easing = AnimationEasing.EmphasizedDecelerate),
        initialScale = 0.8f
    ) + fadeIn(
        animationSpec = tween(duration, easing = AnimationEasing.StandardDecelerate)
    ) togetherWith scaleOut(
        animationSpec = tween(duration / 2, easing = AnimationEasing.EmphasizedAccelerate),
        targetScale = 0.8f
    ) + fadeOut(
        animationSpec = tween(duration / 2, easing = AnimationEasing.StandardAccelerate)
    )

    // Slide - Full screen slide transition
    fun slideTransform(
        forward: Boolean = true,
        duration: Int = AnimationDurations.SMOOTH
    ): ContentTransform {
        return (slideInHorizontally(
            animationSpec = tween(duration, easing = AnimationEasing.EmphasizedDecelerate),
            initialOffsetX = { if (forward) it else -it }
        ) + fadeIn(
            animationSpec = tween(duration, easing = AnimationEasing.StandardDecelerate),
            initialAlpha = 0.3f
        )) togetherWith (slideOutHorizontally(
            animationSpec = tween(duration, easing = AnimationEasing.EmphasizedAccelerate),
            targetOffsetX = { if (forward) -it else it }
        ) + fadeOut(
            animationSpec = tween(duration, easing = AnimationEasing.StandardAccelerate),
            targetAlpha = 0.3f
        ))
    }
}

// Visibility transition specs for AnimatedVisibility
object VisibilityTransitions {
    fun expandVertically(
        duration: Int = AnimationDurations.MEDIUM
    ) = expandVertically(
        animationSpec = tween(duration, easing = AnimationEasing.EmphasizedDecelerate)
    ) + fadeIn(
        animationSpec = tween(duration, easing = AnimationEasing.StandardDecelerate)
    )

    fun shrinkVertically(
        duration: Int = AnimationDurations.FAST
    ) = shrinkVertically(
        animationSpec = tween(duration, easing = AnimationEasing.EmphasizedAccelerate)
    ) + fadeOut(
        animationSpec = tween(duration, easing = AnimationEasing.StandardAccelerate)
    )

    fun slideInFromTop(
        duration: Int = AnimationDurations.MEDIUM
    ) = slideInVertically(
        animationSpec = tween(duration, easing = AnimationEasing.EmphasizedDecelerate),
        initialOffsetY = { -it }
    ) + fadeIn(
        animationSpec = tween(duration, easing = AnimationEasing.StandardDecelerate)
    )

    fun slideOutToTop(
        duration: Int = AnimationDurations.FAST
    ) = slideOutVertically(
        animationSpec = tween(duration, easing = AnimationEasing.EmphasizedAccelerate),
        targetOffsetY = { -it }
    ) + fadeOut(
        animationSpec = tween(duration, easing = AnimationEasing.StandardAccelerate)
    )
}

// Extension functions for common animated values
@Composable
fun animateFloatSmoothly(
    targetValue: Float,
    label: String = "float"
): Float = animateFloatAsState(
    targetValue = targetValue,
    animationSpec = AnimationSpecs.valueChange(),
    label = label
).value

@Composable
fun animateDpSmoothly(
    targetValue: Dp,
    label: String = "dp"
): Dp = animateDpAsState(
    targetValue = targetValue,
    animationSpec = AnimationSpecs.valueChange(),
    label = label
).value

@Composable
fun animateFloatBouncy(
    targetValue: Float,
    label: String = "float"
): Float = animateFloatAsState(
    targetValue = targetValue,
    animationSpec = AnimationSpecs.springBouncy(),
    label = label
).value

@Composable
fun animateDpBouncy(
    targetValue: Dp,
    label: String = "dp"
): Dp = animateDpAsState(
    targetValue = targetValue,
    animationSpec = AnimationSpecs.springBouncy(),
    label = label
).value

