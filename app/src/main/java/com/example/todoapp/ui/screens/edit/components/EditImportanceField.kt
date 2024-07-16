package com.example.todoapp.ui.screens.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.Red
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme


@Composable
fun EditImportanceField(
    importance: Importance,
    uiAction: (EditUiAction) -> Unit
) {
    val isImportant = remember(importance) { importance == Importance.IMPORTANT }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(ExtendedTheme.colors.backPrimary)
            .padding(horizontal = 15.dp)
            .padding(top = 20.dp, bottom = 15.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable { expanded = !expanded }
    ) {
        Text(
            text = stringResource(id = R.string.importance_title),
            color = ExtendedTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(id = importance.toStringResource()),
            modifier = Modifier.padding(top = 5.dp),
            color = if (isImportant) Red else ExtendedTheme.colors.labelTertiary
        )

        EditImportanceBottomSheet(
            initialImportance = importance,
            showModalBottomSheet = expanded,
            uiAction = uiAction,
            closeBottomSheet = { expanded = false }
        )
    }
}

@Preview
@Composable
fun PreviewImportance(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        EditImportanceField(
            importance = Importance.BASIC,
            uiAction = {}
        )
    }
}