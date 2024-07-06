package com.example.todoapp.ui.themes

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Provides preview parameter values for testing different states of the theme in UI previews.
 */


class ThemePreview : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(
        false,
        true
    )
}