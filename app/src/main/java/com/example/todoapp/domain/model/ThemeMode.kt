package com.example.todoapp.domain.model

import com.example.todoapp.R

enum class ThemeMode {
    LIGHT {
        override fun toStringResource(): Int = R.string.system_theme_light
    },
    DARK {
        override fun toStringResource(): Int = R.string.system_theme_dark
    },
    SYSTEM {
        override fun toStringResource(): Int = R.string.system_theme_system
    };

    abstract fun toStringResource(): Int
}