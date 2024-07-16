package com.example.todoapp.ui.screens.list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopAppBar(
    modifier: Modifier = Modifier,
    doneTasks: Int = 0,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    ),
    isFiltered: Boolean = false,
    onVisibilityClick: (Boolean) -> Unit = {},
    navigateToSettings: () -> Unit = {}
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

    val titlePadding = animateDpAsState(
        targetValue = if (isTittleExpand) 44.dp else 0.dp,
        label = "padding animation"
    )

    LargeTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = titlePadding.value,
                    ),
                verticalAlignment = Alignment.Bottom
            ) {
                CollapsedTitle(
                    titleFontSize = fontSize.value.sp,
                    titleLineHeight = lineHeight.value.sp,
                    isExpanded = isTittleExpand,
                    doneTasks = doneTasks,
                    modifier = Modifier.weight(1f)
                )

                ListVisibilityIconButton(
                    modifier = Modifier.height(24.dp),
                    isVisibleOff = isFiltered,
                    onClick = {
                        onVisibilityClick(!isFiltered)
                    }
                )

                SettingsIconButton(
                    modifier = Modifier.height(24.dp),
                    onClick = navigateToSettings
                )
            }

        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = ExtendedTheme.colors.backPrimary,
            scrolledContainerColor = ExtendedTheme.colors.backPrimary
        ),
        modifier = modifier.graphicsLayer {
            this.shadowElevation = elevation.value.toPx()
        }

    )
}

@Composable
fun CollapsedTitle(
    titleFontSize: TextUnit,
    titleLineHeight: TextUnit,
    isExpanded: Boolean,
    doneTasks: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.todolist_title),
            color = ExtendedTheme.colors.labelPrimary,
            style = ExtendedTheme.typography.largeTitle.copy(
                fontSize = titleFontSize,
                lineHeight = titleLineHeight
            )
        )

        AnimatedVisibility(
            visible = isExpanded,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 640, locale = "ru")
@Composable
fun PreviewListScreen(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        ListTopAppBar()
    }
}