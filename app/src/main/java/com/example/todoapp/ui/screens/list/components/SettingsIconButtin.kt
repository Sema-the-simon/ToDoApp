package com.example.todoapp.ui.screens.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.Red
import com.example.todoapp.ui.themes.TodoAppTheme

@Composable
fun SettingsIconButton(
    modifier: Modifier = Modifier,
    color: Color = Blue,
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = color
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Sharp.Settings,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun SettingsIconPreview() {
    TodoAppTheme {
        Column {
            SettingsIconButton {}
            SettingsIconButton(color = Red) {}
        }
    }
}