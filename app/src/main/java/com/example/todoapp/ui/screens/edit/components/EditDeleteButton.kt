package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.LightLabelDisable
import com.example.todoapp.ui.themes.Red


@Composable
fun EditDeleteButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val deleteButtonColor by animateColorAsState(
        targetValue = if (enabled) Red else LightLabelDisable,
        label = "delete_button_color_animation"
    )

    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 5.dp),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = deleteButtonColor,
            disabledContentColor = deleteButtonColor
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete_title),
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = stringResource(id = R.string.delete_title),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Preview
@Composable
fun PreviewDeleteButton() {
    Box(Modifier.background(LightBackPrimary)) {
        EditDeleteButton(
            enabled = false,
            onClick = {}
        )
    }
}