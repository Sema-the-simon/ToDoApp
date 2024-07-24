package com.example.todoapp.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todoapp.ui.themes.ExtendedTheme

@Composable
fun Divider(
    padding: PaddingValues,
    modifier: Modifier = Modifier
        .fillMaxWidth()
) {
    HorizontalDivider(
        modifier = modifier
            .padding(padding),
        color = ExtendedTheme.colors.supportSeparator
    )
}
