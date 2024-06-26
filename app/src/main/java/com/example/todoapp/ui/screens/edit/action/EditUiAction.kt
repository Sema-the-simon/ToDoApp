package com.example.todoapp.ui.screens.edit.action

import com.example.todoapp.data.model.Importance

sealed class EditUiAction {
    data object SaveTask: EditUiAction()
    data object DeleteTask : EditUiAction()
    data class UpdateText(val text: String) : EditUiAction()
    data class UpdateDeadlineSet(val isDeadlineSet: Boolean): EditUiAction()
    data class UpdateDeadline(val deadline: Long) : EditUiAction()
    data class UpdateImportance(val importance: Importance) : EditUiAction()
    data class UpdateDialogVisibility(val isDialogVisible: Boolean) : EditUiAction()
}