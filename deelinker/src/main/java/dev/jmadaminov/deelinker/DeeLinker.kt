package dev.jmadaminov.deelinker

import android.net.Uri

fun buildDeeLinker(
    deeplinkUri: Uri,
    root: DeeNode,
    hosts: List<String>,
    vararg deeManuals: DeeManual
): DeeNode? {
    deeManuals.forEach { handler ->
        if (handler.matcher?.invoke(deeplinkUri.toString()) == true || handler.url == deeplinkUri.toString()) {
            handler.onMatch(deeplinkUri.toString())
            return null
        }
    }

    var start: DeeNode? = null
    var currentNode: DeeNode? = null
    deeplinkUri.host?.let { host ->
        if (!hosts.contains(host)) {
            currentNode = root.possibleDirections.firstOrNull { it.segment == host }
            currentNode?.setIdParam(null)
            currentNode?.setQuery(deeplinkUri.query)
        }
    }

    deeplinkUri.path?.split("/")?.forEach lit@{ pathEntry ->
        if (listOf("ru", "uz", "en").contains(pathEntry)) return@lit
        pathEntry.toLongOrNull()?.let {
            currentNode?.setIdParam(pathEntry)
            return@lit
        } ?: run {
            if (pathEntry.isNotBlank()) {
                currentNode = if (currentNode == null) {
                    val node = root.possibleDirections.firstOrNull { it.segment == pathEntry }
                    node?.nextNode = null
                    node?.setIdParam(null)
                    node?.setQuery(null)
                    node
                } else {
                    val temp = object : DeeNode {
                        override var segment = pathEntry
                        override var nextNode: DeeNode? = null
                        override val possibleDirections: MutableList<DeeNode> = mutableListOf()
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
    return start
}