package com.example.todoapp.data.model

import com.example.todoapp.R

enum class Importance {
    LOW {
        override fun toStringResource(): Int = R.string.importance_low
    },
    BASIC {
        override fun toStringResource(): Int = R.string.importance_normal
    },
    IMPORTANT {
        override fun toStringResource(): Int = R.string.importance_urgent
    };

    abstract fun toStringResource(): Int
}