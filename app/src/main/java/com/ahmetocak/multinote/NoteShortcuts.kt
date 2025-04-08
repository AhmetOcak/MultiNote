package com.ahmetocak.multinote

enum class KeyboardShortCuts(private val symbol: String) {
    DOT("• "),
    DASH("- "),
    SLASH("/"),
    PIPE("|"),
    START("*"),
    HASH("# "),
    THREE_DOT("...");

    override fun toString(): String {
        return symbol
    }
}

enum class BracketShortCuts(private val symbol: String) {
    BRACKET_ONE("( )"),
    BRACKET_TWO("[ ]"),
    BRACKET_THREE("{ }");

    override fun toString(): String {
        return symbol
    }
}

enum class CharacterEffects(private val symbol: String) {
    ITALIC("\uD835\uDC70"),
    BOLD("\uD835\uDDD5"),
    WORD_COLOR("\uD83D\uDD35");

    override fun toString(): String {
        return symbol
    }
}