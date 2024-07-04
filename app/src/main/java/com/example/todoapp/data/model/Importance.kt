package com.example.todoapp.data.model

import com.example.todoapp.R

enum class Importance {
    LOW {
        override fun toStringResource(): Int = R.string.importance_low
        override fun toServerFormatString() = "low"
    },
    BASIC {
        override fun toStringResource(): Int = R.string.importance_normal
        override fun toServerFormatString() = "basic"

    },
    IMPORTANT {
        override fun toStringResource(): Int = R.string.importance_urgent
        override fun toServerFormatString() = "important"
    };

    abstract fun toStringResource(): Int
    abstract fun toServerFormatString(): String

}