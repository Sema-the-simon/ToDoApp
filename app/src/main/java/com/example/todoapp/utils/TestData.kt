package com.example.todoapp.utils

import android.icu.util.Calendar
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import kotlin.random.Random

fun getData(): MutableList<TodoItem> {
    val data = mutableListOf<TodoItem>()
    val texts = listOf(
        "Купить что-то",
        "Купить что-то где-то, но зачем не очень понятно",
        "Купить что-то где-то, но зачем не очень понятно, но точно чтобы показать как обрезается"
    )

    data.add(
        TodoItem(
            id = 0.toString(),
            text = texts[0],
            importance = Importance.BASIC,
            isDone = true,
            creationDate = Calendar.getInstance().also { it.set(Calendar.DAY_OF_WEEK, -1) }.timeInMillis
        )
    )

    data.add(
        TodoItem(
            id = 1.toString(),
            text = texts[0],
            importance = Importance.BASIC,
            isDone = false ,
            creationDate = Calendar.getInstance().also { it.set(Calendar.DAY_OF_WEEK, -2) }.timeInMillis
        )
    )

    data.add(
        TodoItem(
            id = 2.toString(),
            text = texts[1],
            importance = Importance.BASIC,
            isDone = false ,
            creationDate = Calendar.getInstance().also { it.set(Calendar.DAY_OF_WEEK, -3) }.timeInMillis
        )
    )

    data.add(
        TodoItem(
            id = 3.toString(),
            text = texts[2],
            importance = Importance.BASIC,
            isDone = false ,
            creationDate = Calendar.getInstance().also { it.set(Calendar.DAY_OF_WEEK, -4) }.timeInMillis
        )
    )

    data.add(
        TodoItem(
            id = 4.toString(),
            text = texts[0],
            importance = Importance.IMPORTANT,
            isDone = false ,
            creationDate = Calendar.getInstance().timeInMillis
        )
    )

    data.add(
        TodoItem(
            id = 5.toString(),
            text = texts[0],
            importance = Importance.LOW,
            isDone = false ,
            creationDate = Calendar.getInstance().timeInMillis
        )
    )

    data.add(
        TodoItem(
            id = 6.toString(),
            text = texts[0],
            importance = Importance.BASIC,
            deadline = Calendar.getInstance().also { it.add(Calendar.DAY_OF_MONTH, 2) }.timeInMillis,
            isDone = false ,
            creationDate = Calendar.getInstance().timeInMillis
        )
    )

    for (i in 7..15) {
        data.add(
            TodoItem(
                id = i.toString(),
                text = texts.random(),
                importance = Importance.entries.random(),
                creationDate = Calendar.getInstance().timeInMillis,
                isDone = Random.nextBoolean()
            )
        )
    }
    return data
}