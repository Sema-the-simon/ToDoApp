package com.example.todoapp.ui.screens.list.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.todoapp.R
import com.example.todoapp.ui.themes.Blue

@Composable
fun ListVisibilityIconButton(
    modifier: Modifier = Modifier,
    isVisibleOff: Boolean = false,
    color: Color = Blue,
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = color
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                id = if (isVisibleOff)
                    R.drawable.visibility_off
                else
                    R.drawable.visibility
            ),
            contentDescription = "Filter done tasks"
        )
    }
}