package com.example.todoapp.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.screens.edit.components.EditImportanceField
import com.example.todoapp.ui.screens.edit.components.EditTextField
import com.example.todoapp.ui.screens.edit.components.EditTopAppBar
import com.example.todoapp.ui.themes.LightBackPrimary

@Composable
fun EditScreen(
    navController: NavHostController,
    uiState: EditUiState,
    onUiAction: (EditUiAction) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            EditTopAppBar(
                text = uiState.text,
                uiAction = onUiAction,
                navigateUp = { navigateUp() }
            )
        },
        containerColor = LightBackPrimary
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
//            EditDivider(padding = PaddingValues(horizontal = 16.dp))
//            Deadline(
//                deadline = uiState.deadline,
//                isDateVisible = uiState.isDeadlineSet,
//                uiAction = viewModel::onUiAction
//            )
//            EditDivider(padding = PaddingValues(top = 16.dp, bottom = 8.dp))
//            DeleteButton(
//                enabled = uiState.isDeleteEnabled,
//                uiAction = viewModel::onUiAction
//            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewListScreen() {
    EditScreen(rememberNavController(), EditUiState(
        ""
    ), {}, {})
}