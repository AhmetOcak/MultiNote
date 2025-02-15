package com.ahmetocak.multinote.model

data class UserPreferences(
    val isDarkTheme: Boolean,
    val isDynamicColor: Boolean,
    val colorScheme: String
)

enum class ColorSchemeKeys {
    NEON,
    YELLOW,
    PINK,
    ORANGE,
    SEA
}