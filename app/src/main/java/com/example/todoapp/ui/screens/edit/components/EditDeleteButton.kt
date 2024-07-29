package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.Red
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme


@Composable
fun EditDeleteButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val deleteButtonColor by animateColorAsState(
        targetValue = if (enabled) Red else ExtendedTheme.colors.labelDisable,
        label = "delete_button_color_animation"
    )

    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 5.dp),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = deleteButtonColor,
            disabledContentColor = deleteButtonColor,
            containerColor = ExtendedTheme.colors.backPrimary,
            disabledContainerColor = ExtendedTheme.colors.backPrimary
        )
    ) {
        val contentDescription = stringResource(id = R.string.delete_button_description)
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = stringResource(id = R.string.delete_button_text),
            style = ExtendedTheme.typography.button,
            modifier = Modifier.padding(start = 5.dp)
                .semantics {
                    this.contentDescription = contentDescription
                },

        )
    }
}

@Preview
@Composable
fun PreviewDeleteButton(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme {
        Column{
            EditDeleteButton(
                enabled = true,
                onClick = {}
            )
            EditDeleteButton(
                enabled = false,
                onClick = {}
            )
        }
    }
}