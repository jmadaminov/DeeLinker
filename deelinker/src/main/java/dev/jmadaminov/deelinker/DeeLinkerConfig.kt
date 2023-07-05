package dev.jmadaminov.deelinker

data class DeeLinkerConfig(
    var hosts: List<String> = listOf(),
    var deeMatchers: List<DeeMatcher> = listOf(),
    var ignoreSegmentKeys: List<String> = listOf()
)