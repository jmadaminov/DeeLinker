package dev.jmadaminov.deelinker

data class DeeLinkerConfig(
    var hosts: List<String> = listOf(),
    var customHandlers: List<LinkHandler> = listOf(),
    var ignoreSegmentKeys: List<String> = listOf()
)
