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
        if (handler.predicate(deeplinkUri.toString())) {
            handler.onMatch(deeplinkUri)
            return
        }
    }

    val rootNodes = EnumSet.allOf(E::class.java).map { it as DeeNode }
    var start: DeeNode? = null
    var currentNode: DeeNode? = null
    deeplinkUri.host?.let { host ->
        if (!config.hosts.contains(host)) {
            currentNode = rootNodes.firstOrNull { it.segment == host }
            currentNode?.setIdSegment(null)
            currentNode?.setQuery(deeplinkUri.query)
        }
    }

    deeplinkUri.path?.split("/")?.forEach lit@{ pathEntry ->
        if (config.ignoreSegmentKeys.contains(pathEntry)) return@lit
        pathEntry.toLongOrNull()?.let {
            currentNode?.setIdSegment(pathEntry)
            return@lit
        } ?: run {
            if (pathEntry.isNotBlank()) {
                currentNode = if (currentNode == null) {
                    val node = rootNodes.firstOrNull { it.segment == pathEntry }
                    node?.nextNode = null
                    node?.setIdSegment(null)
                    node?.setQuery(null)
                    node
                } else {
                    val temp = object : DeeNode {
                        override var segment = pathEntry
                        override var nextNode: DeeNode? = null
                        override val childNodes: MutableList<DeeNode> = mutableListOf()
                        override var host: String = deeplinkUri.host ?: ""
                    }
                    currentNode?.nextNode = temp
                    temp
                }
            }
            if (start == null) start = currentNode
            currentNode?.setQuery(deeplinkUri.query)
        }
    }
    start?.let {
        onSuccess(it as E)
    } ?: run {
        onFail(deeplinkUri)
    }
}
