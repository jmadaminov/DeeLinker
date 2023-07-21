package dev.jmadaminov.deelinker

data class DeeLinkerConfig(
    var hosts: List<String> = listOf(),
    var customHandlers: List<LinkHandler> = listOf(),
    var segmentAsMetaDataHandlers: List<Pair<String, (String) -> Boolean>> = listOf(),
    var ignoreSegmentKeys: List<String> = listOf()
)
