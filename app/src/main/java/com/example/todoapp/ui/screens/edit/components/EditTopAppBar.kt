package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.R
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.LightLabelDisable
import com.example.todoapp.ui.themes.LightLabelPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopAppBar(
    text: String,
    uiAction: (EditUiAction) -> Unit,
    navigateUp: () -> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = { navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.edit_close_button)
                )
            }
        },
        actions = {
            val saveButtonColor by animateColorAsState(
                targetValue = if (text.isBlank()) LightLabelDisable else Blue,
                label = "save_button_color_animation"
            )

            TextButton(
                onClick = { uiAction(EditUiAction.SaveTask) },
                enabled = text.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = saveButtonColor,
                    disabledContentColor = saveButtonColor
                )
            ) {
                Text(
                    text = stringResource(R.string.edit_save_button),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LightBackPrimary,
            navigationIconContentColor = LightLabelPrimary
        )
    )
}

@Preview
@Composable
fun PreviewEditTopAppBar() {
    EditTopAppBar("", {}, {})
}