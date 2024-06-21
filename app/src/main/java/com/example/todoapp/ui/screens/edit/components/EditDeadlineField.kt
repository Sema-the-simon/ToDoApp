package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.BlueTranslucent
import com.example.todoapp.ui.themes.LightBackElevated
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.LightLabelPrimary
import com.example.todoapp.ui.themes.LightSupportOverlay
import com.example.todoapp.utils.formatLongToDatePattern


@Composable
fun EditDeadlineField(
    deadline: Long,
    isDeadlineSet: Boolean,
    uiAction: (EditUiAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dateText = remember(deadline) { formatLongToDatePattern(deadline) }
        var isDialogOpen by remember { mutableStateOf(false) }

        Column(
            if (isDeadlineSet)
                Modifier.clickable {
                    isDialogOpen = true
                }
            else
                Modifier
        ) {
            Text(
                text = stringResource(id = R.string.deadline_title),
                modifier = Modifier.padding(start = 5.dp),
                color = LightLabelPrimary
            )
            AnimatedVisibility(visible = isDeadlineSet) {
                Box(modifier = Modifier.padding(5.dp)) {
                    Text(text = dateText, color = Blue)
                }
            }
        }
        Switch(
            checked = isDeadlineSet,
            onCheckedChange = { checked ->
                if (checked) {
                    isDialogOpen = true
                } else {
                    uiAction(EditUiAction.UpdateDeadlineSet(false))
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blue,
                checkedTrackColor = BlueTranslucent,
                uncheckedThumbColor = LightBackElevated,
                uncheckedTrackColor = LightSupportOverlay,
                uncheckedBorderColor = LightSupportOverlay,
            )
        )
        DeadlineDatePicker(
            isDialogOpen = isDialogOpen,
            deadline = deadline,
            uiAction = uiAction,
            closeDialog = { isDialogOpen = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineDatePicker(
    isDialogOpen: Boolean,
    deadline: Long,
    uiAction: (EditUiAction) -> Unit,
    closeDialog: () -> Unit
) {
    if (isDialogOpen) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = deadline
        )
        val confirmEnabled by remember(datePickerState.selectedDateMillis) {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = closeDialog,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            uiAction(EditUiAction.UpdateDeadline(it))
                            uiAction(EditUiAction.UpdateDeadlineSet(true))
                        }
                        closeDialog()
                    },
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.deadline_calendar_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closeDialog
                ) {
                    Text(stringResource(R.string.deadline_calendar_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview
@Composable
fun PreviewDeadline() {
    Box(Modifier.background(LightBackPrimary)) {
        EditDeadlineField(
            deadline = 1696693800000L,
            isDeadlineSet = true,
            uiAction = {}
        )
    }
}