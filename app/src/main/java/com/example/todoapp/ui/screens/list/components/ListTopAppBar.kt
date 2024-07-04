package com.example.todoapp.ui.screens.list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopAppBar(
    doneTasks: Int = 0,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    ),
    isFiltered: Boolean = false,
    onVisibilityClick: (Boolean) -> Unit = {}
) {
    val isTittleExpand = if (scrollBehavior.state.collapsedFraction < 0.8f) true else false

    val fontSize = animateFloatAsState(
        targetValue = if (isTittleExpand)
            ExtendedTheme.typography.largeTitle.fontSize.value
        else
            ExtendedTheme.typography.title.fontSize.value,
        label = "TitleFontSize"
    )
    val lineHeight = animateFloatAsState(
        targetValue =
        if (isTittleExpand)
            ExtendedTheme.typography.largeTitle.lineHeight.value
        else
            ExtendedTheme.typography.title.lineHeight.value,
        label = "TitleLineHeight"
    )

    val isShadowNeed = scrollBehavior.state.contentOffset < 0
    val elevation = animateDpAsState(
        targetValue = if (isShadowNeed) {
            20.dp
        } else {
            0.dp
        },
        label = "elevation"
    )

    LargeTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = animateDpAsState(
                            targetValue = if (isTittleExpand) 44.dp else 0.dp,
                            label = "padding animation"
                        ).value,
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier

                ) {
                    Text(
                        text = stringResource(R.string.todolist_title),
                        color = ExtendedTheme.colors.labelPrimary,
                        style = ExtendedTheme.typography.largeTitle.copy(
                            fontSize = fontSize.value.sp,
                            lineHeight = lineHeight.value.sp
                        )
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
                            color = ExtendedTheme.colors.labelTertiary,
                            style = ExtendedTheme.typography.body,
                            modifier = Modifier.padding(top = 4.dp)
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
            containerColor = ExtendedTheme.colors.backPrimary,
            scrolledContainerColor = ExtendedTheme.colors.backPrimary
        ),
        modifier = Modifier.graphicsLayer {
            this.shadowElevation = elevation.value.toPx()
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 640, locale = "ru")
@Composable
fun PreviewListScreen(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        ListTopAppBar(0)
    }
}