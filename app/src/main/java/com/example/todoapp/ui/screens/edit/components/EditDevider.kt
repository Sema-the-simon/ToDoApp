package com.example.todoapp.ui.screens.edit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todoapp.ui.themes.ExtendedTheme

@Composable
fun EditDivider(
    padding: PaddingValues
) {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        color = ExtendedTheme.colors.supportSeparator
    )
}
