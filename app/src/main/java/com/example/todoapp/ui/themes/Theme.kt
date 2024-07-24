package com.example.todoapp.ui.themes

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

/**
 * Defines and provides theme-related functionalities for the TodoApp.
 * It includes color schemes, typography styles, and previews for the theme components.
 *
 * - **`ExtendedTheme`**: Provides access to the extended colors and typography used in the theme.
 * - **`TodoAppTheme`**: Composable function that sets up the theme based on the dark mode and dynamic color availability.
 */


object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
    val typography: ExtendedTypography
        @Composable
        get() = LocalExtendedTypography.current

    val isDarkTheme: Boolean
        @Composable
        get() = LocalThemeScheme.current
}

val LocalThemeScheme = staticCompositionLocalOf {
    true
}

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors =
        if (darkTheme) darkExtendedColors else lightExtendedColors

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }

        }
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography,
        LocalThemeScheme provides darkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

@Preview
@Composable
fun TextPreview() {
    TodoAppTheme {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(ExtendedTheme.colors.backPrimary)
                .padding(8.dp)
        ) {
            Text(text = "Текстовые стили:")
            HorizontalDivider(Modifier.width(200.dp))
            val textModifier = Modifier.padding(12.dp)
            Text(
                text = "Large Title",
                style = ExtendedTheme.typography.largeTitle,
                modifier = textModifier
            )
            Text(
                text = "Title",
                style = ExtendedTheme.typography.title,
                modifier = textModifier
            )
            Text(
                text = "BUTTON",
                style = ExtendedTheme.typography.button,
                modifier = textModifier
            )
            Text(
                text = "Body",
                style = ExtendedTheme.typography.body,
                modifier = textModifier
            )
            Text(
                text = "Subhead",
                style = ExtendedTheme.typography.subhead,
                modifier = textModifier
            )
        }

    }
}

@Preview
@Composable
fun ColorPalettePreview(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        val color = if (isDarkTheme) "темная" else "светлая"
        Column(Modifier.background(GrayLight)) {
            Text(text = "Палитра — $color тема:")
            HorizontalDivider(
                Modifier
                    .width(250.dp)
                    .padding(8.dp)
            )
            Row {
                BoxColorPreview("Support / Separator", ExtendedTheme.colors.supportSeparator)
                BoxColorPreview("Support / Overlay", ExtendedTheme.colors.supportOverlay)
            }
            Row {
                BoxColorPreview("Back / Primary", ExtendedTheme.colors.backPrimary)
                BoxColorPreview("Back / Secondary", ExtendedTheme.colors.backSecondary)
                BoxColorPreview("Back / Elevated", ExtendedTheme.colors.backElevated)
            }
            Row {
                BoxColorPreview(text = "Label / Primary", color = ExtendedTheme.colors.labelPrimary)
                BoxColorPreview(
                    text = "Label / Secondary",
                    color = ExtendedTheme.colors.labelSecondary
                )
                BoxColorPreview(
                    text = "Label / Tertiary",
                    color = ExtendedTheme.colors.labelTertiary
                )
                BoxColorPreview(text = "Label / Disable", color = ExtendedTheme.colors.labelDisable)
            }
            Row {
                BoxColorPreview(text = "Color / Red", color = Red)
                BoxColorPreview(text = "Color / Green", color = Green)
                BoxColorPreview(text = "Color / Blue", color = Blue)
            }
            Row {
                BoxColorPreview(text = "Color / Gray", color = Gray)
                BoxColorPreview(text = "Color / GrayLight", color = GrayLight)
                BoxColorPreview(text = "Color / White", color = White)
            }
        }
    }
}

@Composable
fun BoxColorPreview(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = Color.Yellow,
        style = TextStyle(shadow = Shadow(Color.Black, Offset(2f, 3f), 2f)),
        modifier = Modifier
            .background(color)
            .size(80.dp)

    )
}


