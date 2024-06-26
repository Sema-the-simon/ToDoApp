package com.example.todoapp.ui.screens.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.themes.GrayLight
import com.example.todoapp.ui.themes.Green
import com.example.todoapp.ui.themes.Red
import com.example.todoapp.ui.themes.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(
    dismissState: SwipeToDismissBoxState,
    isTaskDone: Boolean
) {
    val direction = dismissState.dismissDirection
    val color = when (direction) {
        SwipeToDismissBoxValue.EndToStart -> Red
        SwipeToDismissBoxValue.StartToEnd -> if (isTaskDone) GrayLight else Green
        SwipeToDismissBoxValue.Settled -> Green
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == SwipeToDismissBoxValue.StartToEnd) Icon(
            if (isTaskDone) Icons.Default.Close else Icons.Default.Done,
            contentDescription = "change task status",
            tint = White
        )
        Spacer(modifier = Modifier)
        if (direction == SwipeToDismissBoxValue.EndToStart) Icon(
            Icons.Default.Delete,
            contentDescription = "delete task",
            tint = White
        )
    }
}