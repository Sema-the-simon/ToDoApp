package com.example.todoapp.ui.screens.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.LightSupportSeparator

@Composable
fun EditDivider(
    padding: PaddingValues
) {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        color = LightSupportSeparator
    )
}

@Preview
@Composable
fun PreviewEditDivider() {
    Box(Modifier.background(LightBackPrimary)) {
        EditDivider(padding = PaddingValues(all = 15.dp))
    }
}
