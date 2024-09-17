package com.example.todoapp.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.common.Divider
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
    navigateBack: () -> Unit,
    navigateToInfo: () -> Unit
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
            Divider(
                PaddingValues(vertical = 8.dp, horizontal = 24.dp),
            )
            val onClickLabel = stringResource(R.string.info_button_description)
            Row(
                modifier = Modifier
                    .padding(all = 15.dp)
                    .clickable(onClickLabel = onClickLabel) { navigateToInfo() }
            ) {
                Text(text = stringResource(R.string.navigate_info_button))
            }
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
            navigateBack = {},
            {}
        )
    }
}