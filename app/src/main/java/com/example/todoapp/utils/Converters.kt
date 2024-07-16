package com.example.todoapp.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.ThemeMode
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Utility functions and constants.
 */


const val IMPORTANCE_IMPORTANT_ID = 3
const val IMPORTANCE_BASIC_ID = 2
const val IMPORTANCE_LOW_ID = 1


fun formatLongToDatePattern(date: Long): String =
    SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(date)

fun String.toImportance(): Importance =
    when (this) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.BASIC
        else -> Importance.LOW
    }

fun Importance.getImportanceId(): Int =
    when (this) {
        Importance.IMPORTANT -> IMPORTANCE_IMPORTANT_ID
        Importance.BASIC -> IMPORTANCE_BASIC_ID
        else -> IMPORTANCE_LOW_ID
    }

@Composable
@ReadOnlyComposable
fun isDarkThemeSetUp(themeMode: ThemeMode): Boolean = when (themeMode) {
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
    else -> isSystemInDarkTheme()
}


