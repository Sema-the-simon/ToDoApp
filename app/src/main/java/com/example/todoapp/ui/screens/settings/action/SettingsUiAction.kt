package com.example.todoapp.ui.screens.settings.action

import com.example.todoapp.domain.model.ThemeMode

sealed class SettingsUiAction {
    data class UpdateThemeMode(val themeMode: ThemeMode) : SettingsUiAction()
}