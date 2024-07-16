package com.example.todoapp.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.todoapp.ui.screens.settings.action.SettingsUiAction
import com.example.todoapp.ui.screens.settings.components.SettingsTopAppBar
import com.example.todoapp.ui.screens.settings.components.ThemePicker
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme


@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onUiAction: (SettingsUiAction) -> Unit,
    navigateBack: () -> Unit
) {

    Scaffold(
        topBar = { SettingsTopAppBar(navigateBack = navigateBack) },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            ThemePicker(
                themeMode = uiState.themeMode,
                uiAction = onUiAction
            )
        }
    }
}

@Preview
@Composable
fun SettingsPreview(
    @PreviewParameter(ThemePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        SettingsScreen(
            uiState = SettingsUiState(),
            onUiAction = {},
            navigateBack = {}
        )
    }
}