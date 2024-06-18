package com.example.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun formatLongToDatePattern(date: Long): String =
    SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(date)

fun unixToDate(date: Long) = Date(date * MS_IN_S)

fun dateToUnix(date: Date) = date.time / MS_IN_S
