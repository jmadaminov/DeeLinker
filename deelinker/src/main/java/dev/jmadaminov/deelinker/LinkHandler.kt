package dev.jmadaminov.deelinker

import android.net.Uri

data class LinkHandler(
    val predicate: ((Uri) -> Boolean),
    val onMatch: (Uri) -> Unit
)
