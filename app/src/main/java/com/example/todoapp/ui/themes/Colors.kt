package com.example.todoapp.ui.themes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Defines color values and provides a structured way to manage color schemes for the TodoApp.
 * Includes light and dark color palettes and an `ExtendedColors` data class to encapsulate them.
 */


val Red = Color(0xFFFF3B30)
val Green = Color(0xFF34C759)
val Blue = Color(0xFF007AFF)
val BlueTranslucent = Color(0x4D007AFF)
val Gray = Color(0xFF8E8E93)
val GrayLight = Color(0xFFD1D1D6)
val White = Color(0xFFFFFFFF)

val LightSupportSeparator = Color(0x33000000)
val LightSupportOverlay = Color(0x0F000000)
val LightLabelPrimary = Color(0xFF000000)
val LightLabelSecondary = Color(0x99000000)
val LightLabelTertiary = Color(0x4D000000)
val LightLabelDisable = Color(0x26000000)
val LightBackPrimary = Color(0xFFF7F6F2)
val LightBackSecondary = Color(0xFFFFFFFF)
val LightBackElevated = Color(0xFFFFFFFF)

val DarkSupportSeparator = Color(0x33FFFFFF)
val DarkSupportOverlay = Color(0x52000000)
val DarkLabelPrimary = Color(0xFFFFFFFF)
val DarkLabelSecondary = Color(0x99FFFFFF)
val DarkLabelTertiary = Color(0x66FFFFFF)
val DarkLabelDisable = Color(0x26FFFFFF)
val DarkBackPrimary = Color(0xFF161618)
val DarkBackSecondary = Color(0xFF252528)
val DarkBackElevated = Color(0xFF3C3C3F)

@Immutable
data class ExtendedColors(
    val supportSeparator: Color = Color.Unspecified,
    val supportOverlay: Color = Color.Unspecified,
    val labelPrimary: Color = Color.Unspecified,
    val labelSecondary: Color = Color.Unspecified,
    val labelTertiary: Color = Color.Unspecified,
    val labelDisable: Color = Color.Unspecified,
    val backPrimary: Color = Color.Unspecified,
    val backSecondary: Color = Color.Unspecified,
    val backElevated: Color = Color.Unspecified
)

val lightExtendedColors = ExtendedColors(
    supportSeparator = LightSupportSeparator,
    supportOverlay = LightSupportOverlay,
    labelPrimary = LightLabelPrimary,
    labelSecondary = LightLabelSecondary,
    labelTertiary = LightLabelTertiary,
    labelDisable = LightLabelDisable,
    backPrimary = LightBackPrimary,
    backSecondary = LightBackSecondary,
    backElevated = LightBackElevated
)

val darkExtendedColors = ExtendedColors(
    supportSeparator = DarkSupportSeparator,
    supportOverlay = DarkSupportOverlay,
    labelPrimary = DarkLabelPrimary,
    labelSecondary = DarkLabelSecondary,
    labelTertiary = DarkLabelTertiary,
    labelDisable = DarkLabelDisable,
    backPrimary = DarkBackPrimary,
    backSecondary = DarkBackSecondary,
    backElevated = DarkBackElevated
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors()
}

val LightColorScheme = lightColorScheme(
    background = LightBackPrimary,
    onBackground = LightLabelPrimary,
    surface = LightBackPrimary,
    onSurface = LightLabelPrimary,
    surfaceTint = DarkLabelTertiary,
    surfaceContainerHigh = Blue,
    onSurfaceVariant = White,
    inverseSurface = DarkBackPrimary,
    inverseOnSurface = DarkLabelPrimary,
    primary = Blue,
    primaryContainer = Blue,
    onPrimaryContainer = White,
    onPrimary = White,
    inversePrimary = White,
    secondary = LightBackSecondary,
    onSecondary = LightLabelSecondary,
    error = Red,
    onError = White,
    onErrorContainer = White,
    errorContainer = Red,
)
val DarkColorScheme = darkColorScheme(
    background = DarkBackPrimary,
    onBackground = DarkLabelPrimary,
    surface = DarkBackPrimary,
    onSurface = DarkLabelPrimary,
    surfaceTint = LightLabelTertiary,
    surfaceContainerHigh = Blue,
    onSurfaceVariant = White,
    inverseSurface = LightBackPrimary,
    inverseOnSurface = LightLabelPrimary,
    primary = Blue,
    primaryContainer = Blue,
    onPrimaryContainer = White,
    onPrimary = White,
    inversePrimary = White,
    secondary = DarkBackSecondary,
    onSecondary = DarkLabelSecondary,
    error = Red,
    onError = White,
    onErrorContainer = White,
    errorContainer = Red,
)