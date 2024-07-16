package com.example.todoapp.ui.screens.edit.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.Green
import com.example.todoapp.ui.themes.Red
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditImportanceBottomSheet(
    initialImportance: Importance,
    showModalBottomSheet: Boolean,
    uiAction: (EditUiAction) -> Unit,
    closeBottomSheet: () -> Unit
) {
    var currentImportance by remember { mutableStateOf(initialImportance) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                uiAction(EditUiAction.UpdateImportance(currentImportance))
                closeBottomSheet()
            },
            sheetState = bottomSheetState,
            containerColor = ExtendedTheme.colors.backPrimary
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxHeight(0.2f),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible)
                                    closeBottomSheet()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.importance_close_button),
                            tint = Red
                        )
                    }

                    IconButton(
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    uiAction(EditUiAction.UpdateImportance(currentImportance))
                                    closeBottomSheet()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(id = R.string.importance_save_button),
                            tint = Green
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (importance in Importance.entries) {
                        ImportanceItem(
                            changeImportance = { currentImportance = importance },
                            importance = importance,
                            selected = currentImportance == importance,
                            modifier = Modifier.weight(0.3f)
                        )
                        if (importance != Importance.IMPORTANT)
                            Spacer(Modifier.weight(0.05f))
                    }
                }
            }
        }
    }
}

@Composable
fun ImportanceItem(
    changeImportance: () -> Unit,
    importance: Importance,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = when {
        selected && importance == Importance.IMPORTANT -> Red
        selected -> ExtendedTheme.colors.labelPrimary
        else -> ExtendedTheme.colors.labelTertiary
    }
    val size = remember { Animatable(initialValue = 1f) }

    val baseBackColor = ExtendedTheme.colors.supportOverlay
    val backgroundColor = remember { Animatable(initialValue = baseBackColor) }
    val scope = rememberCoroutineScope()

    val baseModifier = modifier
        .scale(scale = size.value)
        .background(backgroundColor.value, RoundedCornerShape(32.dp))
        .clip(RoundedCornerShape(32.dp))
        .clickable {
            changeImportance()
            scope.launch {
                size.animateBounce(0.9f, 1f)
            }
            if (importance == Importance.IMPORTANT) {
                scope.launch {
                    backgroundColor.animateBounce(Red.copy(alpha = 0.5f), baseBackColor, 200)
                }
            }
        }

    Box(
        modifier = baseModifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = importance.toStringResource()),
            modifier = Modifier.padding(all = 10.dp),
            color = color,
            style = ExtendedTheme.typography.body
        )
    }
}

private suspend fun <T, V : AnimationVector> Animatable<T, V>.animateBounce(
    firstValue: T,
    secondValue: T,
    duration: Int = 100
) {
    animateTo(
        targetValue = firstValue,
        animationSpec = tween(
            durationMillis = duration
        )
    )
    animateTo(
        targetValue = secondValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
}