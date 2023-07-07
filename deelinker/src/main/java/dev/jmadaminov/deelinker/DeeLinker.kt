package dev.jmadaminov.deelinker

import android.net.Uri
import java.util.EnumSet

inline fun <reified E : Enum<E>> buildDeeLinker(
    deeplinkUri: Uri,
    config: DeeLinkerConfig = DeeLinkerConfig()
): E? {
    config.deeMatchers.forEach { handler ->
        if (handler.matcher?.invoke(deeplinkUri.toString()) == true || handler.url == deeplinkUri.toString()) {
            handler.onMatch(deeplinkUri.toString())
            return null
        }
    }

    val rootNodes = EnumSet.allOf(E::class.java).map { it as DeeNode }
    var start: DeeNode? = null
    var currentNode: DeeNode? = null
    deeplinkUri.host?.let { host ->
        if (!config.hosts.contains(host)) {
            currentNode = rootNodes.firstOrNull { it.segment == host }
            currentNode?.setIdParam(null)
            currentNode?.setQuery(deeplinkUri.query)
        }
    }

    deeplinkUri.path?.split("/")?.forEach lit@{ pathEntry ->
        if (config.ignoreSegmentKeys.contains(pathEntry)) return@lit
        pathEntry.toLongOrNull()?.let {
            currentNode?.setIdParam(pathEntry)
            return@lit
        } ?: run {
            if (pathEntry.isNotBlank()) {
                currentNode = if (currentNode == null) {
                    val node = rootNodes.firstOrNull { it.segment == pathEntry }
                    node?.nextNode = null
                    node?.setIdParam(null)
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
    return start as? E?
}
