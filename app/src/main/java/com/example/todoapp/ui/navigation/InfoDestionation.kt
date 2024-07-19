package com.example.todoapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.info.CustomDivActionHandler
import com.example.todoapp.ui.screens.info.getDivView
import com.example.todoapp.ui.themes.ExtendedTheme
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.data.Variable

fun NavGraphBuilder.infoDestination(
    onNavigateToList: () -> Unit
) {
    composable<Destination.Info> {
        val lifecycleOwner = LocalLifecycleOwner.current
        val customActionHandler = CustomDivActionHandler(onNavigateToList)
        val variableController = DivVariableController()
        val theme = Variable.StringVariable("app_theme", "light")
        val isDarkTheme = ExtendedTheme.isDarkTheme
        Scaffold { padding ->
            AndroidView(
                factory = { context ->
                    getDivView(
                        context,
                        lifecycleOwner,
                        customActionHandler,
                        variableController,
                        theme
                    )
                },
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                theme.set(if (isDarkTheme) "dark" else "light")
                variableController.putOrUpdate(theme)
            }
        }
    }
}