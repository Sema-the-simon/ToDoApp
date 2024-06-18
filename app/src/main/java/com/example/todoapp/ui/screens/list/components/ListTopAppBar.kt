package com.example.todoapp.ui.screens.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.ui.themes.LightBackPrimary
import com.example.todoapp.ui.themes.LightLabelPrimary
import com.example.todoapp.ui.themes.LightLabelTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopAppBar(
    doneTasks: Int = 0,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    LargeTopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier //.padding(start = startPadding, top = topPadding)
                ) {
                    Text(
                        text = stringResource(R.string.todolist_title),
                        color = LightLabelPrimary,
                        fontSize = 32.sp
                    )
                    Text(
                        text = stringResource(R.string.list_title_tasks_count, doneTasks),
                        color = LightLabelTertiary,
                        fontSize = 14.sp
                    )
                }
//                   TODO VisibilityIcon
            }

        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = LightBackPrimary,
        )
    )
}