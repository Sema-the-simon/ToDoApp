package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.BlueTranslucent
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.Gray
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme
import com.example.todoapp.ui.themes.White
import com.example.todoapp.utils.formatLongToDatePattern


@Composable
fun EditDeadlineField(
    deadline: Long?,
    isDeadlineSet: Boolean,
    isDialogOpen: Boolean,
    uiAction: (EditUiAction) -> Unit
) {
    val deadlineState = if (isDeadlineSet) stringResource(R.string.enable) else
        stringResource(R.string.disable)
    Row(
        modifier = Modifier
            .background(ExtendedTheme.colors.backPrimary)
            .fillMaxWidth()
            .padding(all = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dateText = remember(deadline) {
            if (deadline != null) {
                formatLongToDatePattern(deadline)
            } else ""
        }
        val onCLickLabel = stringResource(R.string.change_deadline)
        Column(
            if (isDeadlineSet)
                Modifier.clickable(onClickLabel = onCLickLabel) {
                    uiAction(EditUiAction.UpdateDialogVisibility(true))
                }
            else
                Modifier
        ) {
            Text(
                text = stringResource(id = R.string.deadline_title),
                modifier = Modifier
                    .padding(start = 5.dp)
                    .semantics {
                        stateDescription = deadlineState
                    },
                color = ExtendedTheme.colors.labelPrimary
            )
            AnimatedVisibility(visible = isDeadlineSet) {
                Box(modifier = Modifier.padding(5.dp)) {
                    Text(text = dateText, color = Blue)
                }
            }
        }
        val switchDescription = stringResource(R.string.deadline_switch_description)
        Switch(
            checked = isDeadlineSet,
            onCheckedChange = { checked ->
                if (checked && deadline == null) {
                    uiAction(EditUiAction.UpdateDialogVisibility(true))
                }
                uiAction(EditUiAction.UpdateDeadlineSet(checked))
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blue,
                checkedTrackColor = BlueTranslucent,
                uncheckedThumbColor = ExtendedTheme.colors.backElevated,
                uncheckedTrackColor = ExtendedTheme.colors.supportOverlay,
                uncheckedBorderColor = ExtendedTheme.colors.supportOverlay,
            ),
            modifier = Modifier.semantics {
                contentDescription = switchDescription
            }
        )
        if (isDialogOpen) {
            DeadlineDatePicker(
                deadline = deadline,
                uiAction = uiAction,
                onDialogClose = { uiAction(EditUiAction.UpdateDialogVisibility(false)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineDatePicker(
    deadline: Long?,
    initialDate: Long = deadline ?: System.currentTimeMillis(),
    uiAction: (EditUiAction) -> Unit,
    onDialogClose: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )
    val confirmEnabled by remember(datePickerState.selectedDateMillis) {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onDialogClose,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { date ->
                        uiAction(EditUiAction.UpdateDeadline(date))
                    }
                    onDialogClose()
                },
                enabled = confirmEnabled
            ) {
                Text(
                    stringResource(R.string.deadline_calendar_ok),
                    style = ExtendedTheme.typography.button
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    if (deadline == null)
                        uiAction(EditUiAction.UpdateDeadlineSet(false))
                    onDialogClose()
                }
            ) {
                Text(
                    stringResource(R.string.deadline_calendar_cancel),
                    style = ExtendedTheme.typography.button
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = ExtendedTheme.colors.backSecondary
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = ExtendedTheme.colors.backSecondary,
                titleContentColor = ExtendedTheme.colors.labelPrimary,
                headlineContentColor = ExtendedTheme.colors.labelPrimary,
                weekdayContentColor = ExtendedTheme.colors.labelTertiary,
                navigationContentColor = ExtendedTheme.colors.labelPrimary,
                yearContentColor = ExtendedTheme.colors.labelPrimary,
                selectedYearContentColor = White,
                disabledSelectedYearContentColor = ExtendedTheme.colors.supportOverlay,
                selectedYearContainerColor = Blue,
                disabledSelectedYearContainerColor = ExtendedTheme.colors.supportOverlay,
                dayContentColor = ExtendedTheme.colors.labelPrimary,
                disabledDayContentColor = ExtendedTheme.colors.supportOverlay,
                selectedDayContentColor = White,
                disabledSelectedDayContentColor = ExtendedTheme.colors.supportOverlay,
                selectedDayContainerColor = Blue,
                disabledSelectedDayContainerColor = Gray,
                todayContentColor = Blue,
                todayDateBorderColor = Blue,
                dividerColor = ExtendedTheme.colors.supportSeparator
            )
        )
    }

}

@Preview
@Composable
fun PreviewDeadline(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        Column {
            EditDeadlineField(
                deadline = 1696693800000L,
                isDeadlineSet = true,
                isDialogOpen = false,
                uiAction = {}
            )
            Spacer(modifier = Modifier.height(25.dp))
            EditDeadlineField(
                deadline = 169669380000L,
                isDeadlineSet = false,
                isDialogOpen = false,
                uiAction = {}
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Preview
@Composable
fun PreviewDatePicker(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        DeadlineDatePicker(
            deadline = 150000L,
            uiAction = {},
            onDialogClose = {}
        )
    }
}