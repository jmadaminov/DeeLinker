package dev.jmadaminov.deelinker

data class DeeManual(
    val url: String? = null,
    val matcher: ((String) -> Boolean)? = null,
    val onMatch: (String) -> Unit
)