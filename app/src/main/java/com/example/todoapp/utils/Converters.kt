package com.example.todoapp.utils

import com.example.todoapp.data.model.Importance
import java.text.SimpleDateFormat
import java.util.Locale


fun formatLongToDatePattern(date: Long): String =
    SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(date)

fun String.toImportance(): Importance =
    when (this) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.BASIC
        else -> Importance.LOW
    }