package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.todoapp.R
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopAppBar(
    isButtonEnable: Boolean,
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
                targetValue = if (!isButtonEnable) ExtendedTheme.colors.labelDisable else Blue,
                label = "save_button_color_animation"
            )

            val description = stringResource(R.string.save_button_description)
            TextButton(
                onClick = {
                    uiAction(EditUiAction.SaveTask)
                    navigateUp()
                },
                enabled = isButtonEnable,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = saveButtonColor,
                    disabledContentColor = saveButtonColor
                ),
                modifier = Modifier.semantics {
                    heading()
                    contentDescription = description
                }
            ) {
                Text(
                    text = stringResource(R.string.edit_save_button),
                    style = ExtendedTheme.typography.button,
                    modifier = Modifier.clearAndSetSemantics {  }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExtendedTheme.colors.backPrimary,
            navigationIconContentColor = ExtendedTheme.colors.labelPrimary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(locale = "ru")
@Composable
fun PreviewEditTopAppBar(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        EditTopAppBar(true, {}, {})
    }
}