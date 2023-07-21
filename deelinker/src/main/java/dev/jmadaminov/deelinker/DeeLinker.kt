package dev.jmadaminov.deelinker

import android.net.Uri
import java.util.EnumSet

inline fun <reified E : Enum<E>> buildDeeLinker(
    deeplinkUri: Uri,
    config: DeeLinkerConfig = DeeLinkerConfig(),
    onSuccess: (E) -> Unit,
    onFail: (Uri) -> Unit = {},
) {
    config.customHandlers.forEach { handler ->
        if (handler.predicate(deeplinkUri)) {
            handler.onMatch(deeplinkUri)
            return
        }
    }

    val rootNodes = EnumSet.allOf(E::class.java).map { it as DeeNode }
    var start: DeeNode? = null
    var currentNode: DeeNode? = null
    if (deeplinkUri.host != null && !config.hosts.contains(deeplinkUri.host)) {
        currentNode = rootNodes.firstOrNull { it.segment == deeplinkUri.host }?.apply {
            cleanMetaData()
            nextNode = null
            setQuery(deeplinkUri.query)
        }
        start = currentNode
    }

    deeplinkUri.pathSegments.forEach lit@{ pathEntry ->
        if (config.ignoreSegmentKeys.contains(pathEntry) || pathEntry.isBlank()) return@lit
        config.segmentAsMetaDataHandlers.forEach { predicate ->
            if (predicate.second(pathEntry)) {
                currentNode?.setMetaData(predicate.first, pathEntry)
                return@lit
            }
        }
        if (currentNode == null) {
            currentNode = rootNodes.firstOrNull { it.segment == pathEntry }
            currentNode?.cleanMetaData()
        } else {
            val temp = makeNodeFor(pathEntry, deeplinkUri.host ?: "")
            currentNode?.nextNode = temp
            currentNode = temp
        }
        if (start == null) start = currentNode
    }
    currentNode?.setQuery(deeplinkUri.query)
    if (start != null) onSuccess(start as E) else onFail(deeplinkUri)
}

fun makeNodeFor(pathEntry: String, host: String) = object : DeeNode {
    override var segment = pathEntry
    override var nextNode: DeeNode? = null
    override val childNodes: MutableList<DeeNode> = mutableListOf()
    override var host: String = host
}
