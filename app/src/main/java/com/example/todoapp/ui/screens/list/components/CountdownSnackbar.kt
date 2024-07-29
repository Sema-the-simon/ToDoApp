package com.example.todoapp.ui.screens.list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ui.themes.Blue
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.Red
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CountdownSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    durationInSeconds: Int = 5,
    canselAll: () -> Unit = {}
) {

    val totalDuration = remember(durationInSeconds) { durationInSeconds * 1000 }
    var millisRemaining by remember { mutableIntStateOf(totalDuration) }

    LaunchedEffect(data) {
        while (millisRemaining > 0) {
            delay(40)
            millisRemaining -= 40
        }
        data.dismiss()
    }

    val actionLabel = data.visuals.actionLabel
    val actionComposable: (@Composable () -> Unit)? =
        if (actionLabel != null) {
            @Composable {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = ExtendedTheme.colors.labelPrimary
                    ),
                    onClick = { data.performAction() },
                    content = { Text(actionLabel) }
                )
            }
        } else {
            null
        }

    val dismissActionComposable: (@Composable () -> Unit)? =
        if (data.visuals.withDismissAction) {
            @Composable {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = ExtendedTheme.colors.labelPrimary
                    ),
                    onClick = {
                        canselAll()
                        data.dismiss()
                    },
                    content = {
                        Icon(Icons.Rounded.Close, null)
                    }
                )
            }
        } else {
            null
        }

    val timerProgress = millisRemaining.toFloat() / totalDuration.toFloat()

    Snackbar(
        modifier = modifier
            .alpha(timerProgress + 0.5f)
            .padding(12.dp),
        action = actionComposable,
        dismissAction = dismissActionComposable,
        containerColor = ExtendedTheme.colors.backSecondary,
        contentColor = ExtendedTheme.colors.labelSecondary,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SnackbarCountdown(
                timerProgress = timerProgress,
                secondsRemaining = (millisRemaining / 1000) + 1,
                color = Blue
            )
            Text(data.visuals.message, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }

}

@Composable
private fun SnackbarCountdown(
    timerProgress: Float,
    secondsRemaining: Int,
    color: Color,
) {

    val timerColor by animateColorAsState(
        when {
            timerProgress in 0.4f..0.6f -> Color.Yellow
            timerProgress < 0.4f -> Red
            else -> color
        },
        label = "timer color"
    )
    val textMeasurer = rememberTextMeasurer()

    val size = textMeasurer.measure(secondsRemaining.toString()).size.height / 2
    Box(
        modifier = Modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.matchParentSize()) {
            val strokeStyle = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(
                color = timerColor.copy(alpha = 0.12f),
                style = strokeStyle
            )
            drawArc(
                color = timerColor,
                startAngle = -90f,
                sweepAngle = (-360f * timerProgress),
                useCenter = false,
                style = strokeStyle
            )
        }
        Text(
            text = secondsRemaining.toString(),
            style = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                color = timerColor
            )
        )
    }
}

@Preview
@Composable
private fun CountdownSnackbarPreview(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    class MyData(
        override val visuals: SnackbarVisuals = object : SnackbarVisuals {
            override val actionLabel: String
                get() = "Отмена"
            override val duration: SnackbarDuration
                get() = SnackbarDuration.Indefinite
            override val message: String
                get() = "Delete task: \"very Long very Long very Long very Long very Long very Long very Long Text task\""
            override val withDismissAction: Boolean
                get() = true
        }
    ) : SnackbarData {
        override fun dismiss() {}
        override fun performAction() {}
    }

    TodoAppTheme(isDarkTheme) {
        Surface(Modifier.height(100.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                CountdownSnackbar(data = MyData())
            }
        }
    }
}


@Preview
@Composable
fun ScaffoldWithSnackbar() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var count by remember { mutableIntStateOf(0) }
    TodoAppTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    CountdownSnackbar(
                        data = data
                    )

                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "current count is - $count",
                                actionLabel = "CANCEL",
                                withDismissAction = true,
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        count++
                    }
                ) {
                    Text("Show snackbar")
                }
            },
            content = { innerPadding ->
                Text(
                    text = "Snackbar Demo",
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
        )

    }
}
