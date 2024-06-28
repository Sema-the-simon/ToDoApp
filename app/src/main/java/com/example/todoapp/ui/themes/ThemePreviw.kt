package com.example.todoapp.ui.themes

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ThemePreview : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(
        false,
        true
    )
}