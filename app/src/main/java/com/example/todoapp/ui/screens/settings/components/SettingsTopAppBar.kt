package com.example.todoapp.ui.screens.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.todoapp.R
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(
    navigateBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.settings_title)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.settings_close_button)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExtendedTheme.colors.backPrimary,
            navigationIconContentColor = ExtendedTheme.colors.labelPrimary
        )
    )
}

@Preview
@Composable
fun PreviewSettingsTopAppBar(
    @PreviewParameter(ThemePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        SettingsTopAppBar(
            navigateBack = {}
        )
    }
}