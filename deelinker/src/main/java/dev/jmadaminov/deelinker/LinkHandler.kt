package dev.jmadaminov.deelinker

data class LinkHandler(
    val predicate: ((String) -> Boolean),
    val onMatch: (String) -> Unit
)
