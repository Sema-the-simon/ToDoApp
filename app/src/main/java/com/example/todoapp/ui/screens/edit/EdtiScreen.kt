package com.example.todoapp.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.screens.edit.action.EditUiEvent
import com.example.todoapp.ui.screens.edit.components.EditDeadlineField
import com.example.todoapp.ui.screens.edit.components.EditDeleteButton
import com.example.todoapp.ui.screens.edit.components.EditDivider
import com.example.todoapp.ui.screens.edit.components.EditImportanceField
import com.example.todoapp.ui.screens.edit.components.EditTextField
import com.example.todoapp.ui.screens.edit.components.EditTopAppBar
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme
import kotlinx.coroutines.launch

@Composable
fun EditScreen(
    uiState: EditUiState,
    uiEvent: EditUiEvent?,
    onUiAction: (EditUiAction) -> Unit,
    navigateUp: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is EditUiEvent.ShowSnackbar -> launch {
                snackbarHostState.showSnackbar(uiEvent.message)
            }

            null -> Unit
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            EditTopAppBar(
                isButtonEnable = uiState.text.isNotBlank(),
                uiAction = onUiAction,
                navigateUp = navigateUp
            )
        },
        containerColor = ExtendedTheme.colors.backPrimary
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            EditTextField(
                text = uiState.text,
                uiAction = onUiAction
            )
            EditImportanceField(
                importance = uiState.importance,
                uiAction = onUiAction
            )
            EditDivider(padding = PaddingValues(horizontal = 16.dp))
            EditDeadlineField(
                deadline = uiState.deadline,
                isDeadlineSet = uiState.isDeadlineSet,
                isDialogOpen = uiState.isDialogVisible,
                uiAction = onUiAction
            )
            EditDivider(padding = PaddingValues(top = 16.dp, bottom = 8.dp))
            EditDeleteButton(
                enabled = uiState.text.isNotBlank(),
                onClick = {
                    onUiAction(EditUiAction.DeleteTask)
                    navigateUp()
                }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, locale = "ru")
@Composable
fun PreviewListScreen(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        EditScreen(
            EditUiState(
                "Lorem ipsum dolor sit amet, consectetur ",
                isDeadlineSet = true,
                deadline = System.currentTimeMillis()
            ),
            null,
            {},
            {}
        )
    }
}