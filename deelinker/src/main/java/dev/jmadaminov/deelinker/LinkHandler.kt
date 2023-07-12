package dev.jmadaminov.deelinker

import android.net.Uri

data class LinkHandler(
    val predicate: ((String) -> Boolean),
    val onMatch: (Uri) -> Unit
)
