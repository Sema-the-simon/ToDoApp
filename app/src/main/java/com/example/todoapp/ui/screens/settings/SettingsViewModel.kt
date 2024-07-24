package com.example.todoapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.datastore.PreferencesManager
import com.example.todoapp.domain.model.ThemeMode
import com.example.todoapp.ui.screens.settings.action.SettingsUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesManager.userPreferences.collectLatest { userPref ->
                _uiState.value = SettingsUiState(
                    themeMode = userPref.themeMode
                )
            }
        }
    }

    fun onUiAction(action: SettingsUiAction) {
        when (action) {
            is SettingsUiAction.UpdateThemeMode -> {
                _uiState.update {
                    uiState.value.copy(
                        themeMode = action.themeMode
                    )
                }

                viewModelScope.launch {
                    preferencesManager.saveThemeMode(action.themeMode)
                }
            }
        }
    }
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)