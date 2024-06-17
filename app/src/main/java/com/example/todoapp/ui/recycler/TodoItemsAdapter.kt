package com.example.todoapp.ui.recycler

import android.view.View
import com.example.todoapp.data.model.RecyclerItem
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.databinding.TodoitemBinding
import com.example.todoapp.utils.formatLongToDatePattern
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun todoItemAdapterDelegate(itemClickedListener: (TodoItem) -> Unit) =
    adapterDelegateViewBinding<TodoItem, RecyclerItem, TodoitemBinding>(
        { layoutInflater, root -> TodoitemBinding.inflate(layoutInflater, root, false) }
    ) {
        binding.todoText.setOnClickListener {
            itemClickedListener(item)
        }
        bind {
            with(binding) {
                todoText.text = item.text
                checkBox.isChecked = item.isDone
                val deadline = item.deadline
                if (deadline != null) {
                    todoDeadline.visibility = View.VISIBLE
                    todoDeadline.text = formatLongToDatePattern(deadline)
                }
            }
        }
    }