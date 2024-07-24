package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.Red
import com.example.todoapp.utils.toStringResource


@Composable
fun ImportanceDropDownMenu(
    oldImportance: Importance,
    expanded: Boolean,
    onDismiss: () -> Unit,
    uiAction: (EditUiAction) -> Unit,
) {

    DropdownMenu(
        expanded = expanded, onDismissRequest = onDismiss
    ) {
        for (importance in Importance.entries) {
            ImportanceItem(
                isSelected = oldImportance == importance,
                onItemClick = {
                    uiAction(EditUiAction.UpdateImportance(importance))
                    onDismiss()
                },
                importance = importance
            )
        }
    }
}

@Composable
fun ImportanceItem(
    isSelected: Boolean,
    onItemClick: () -> Unit,
    importance: Importance,
) {
    var selected by remember {
        mutableStateOf(isSelected)
    }
    val color = when {
        importance == Importance.IMPORTANT -> Red
        else -> ExtendedTheme.colors.labelPrimary
    }
    val alpha = when {
        selected -> 1f
        else -> 0.4f
    }
    val scale = remember { Animatable(initialValue = 1f) }
    Box(
        modifier = Modifier
            .scale(scale = scale.value)
            .clip(RoundedCornerShape(10.dp))
            .alpha(alpha)
            .clickable {
                selected = !selected
                onItemClick()
            }
    ) {
        Text(
            text = stringResource(id = importance.toStringResource()),
            modifier = Modifier.padding(all = 10.dp),
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    if (importance != Importance.IMPORTANT) Spacer(modifier = Modifier.size(10.dp))
}