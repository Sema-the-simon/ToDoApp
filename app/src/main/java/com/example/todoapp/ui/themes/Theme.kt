package com.example.todoapp.ui.themes

import android.os.Build
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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp


object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
    val typography: ExtendedTypography
        @Composable
        get() = LocalExtendedTypography.current
}

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val extendedColors =
        if (darkTheme) darkExtendedColors else lightExtendedColors

    val isDynamicColorAvailable = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        isDynamicColorAvailable && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        isDynamicColorAvailable && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography
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


