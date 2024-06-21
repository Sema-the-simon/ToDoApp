package com.example.todoapp.ui.screens.list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    isFiltered: Boolean = false,
    onVisibilityClick: (Boolean) -> Unit = {}
) {
    val isTittleExpand = if (scrollBehavior.state.collapsedFraction < 0.07f) true else false
    LargeTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = animateDpAsState(
                            targetValue = if (isTittleExpand) 44.dp else 0.dp
                        ).value
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier

                ) {
                    Text(
                        text = stringResource(R.string.todolist_title),
                        color = LightLabelPrimary,
                        fontSize = (animateIntAsState(
                            targetValue = if (isTittleExpand) 32 else 20,
                            label = "TopAppTitle"
                        )).value.sp
                    )
                    AnimatedVisibility(
                        visible = isTittleExpand,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Text(
                            text = stringResource(
                                R.string.list_title_tasks_count,
                                doneTasks
                            ),
                            color = LightLabelTertiary,
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            modifier = Modifier
                        )
                    }
                }
                ListVisibilityIconButton(
                    modifier = Modifier.height(24.dp),
                    isFiltered
                ) {
                    onVisibilityClick(!isFiltered)
                }
            }

        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = LightBackPrimary,
            scrolledContainerColor = LightBackPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewListScreen() {
    ListTopAppBar(0, TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()))
}