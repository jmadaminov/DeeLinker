package com.example.deeplinkapplication.deeplink

import android.net.Uri

fun buildDeeLinker(deeplinkUri: Uri, vararg deeManuals: DeeManual): DeeNode? {
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
            currentNode = RootDirections.values().firstOrNull { it.segment == host }
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
                    val node = RootDirections.values().firstOrNull { it.segment == pathEntry }
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

private const val UZUM_SCHEME = "uzum"
private const val HTTPS_SCHEME = "https"
private const val HTTP_SCHEME = "http"
private const val UZUM_HOST = "uzum.uz"
private const val UZUM_WEB_HOST = "www.uzum.uz"
private const val UZUM_HOST_STREAM = "live.uzum.uz"
private const val UZUM_NEW_HOST_STREAM = "live2.uzum.uz"

private const val ADJUST_SCHEME = "android-app"
private const val ADJUST_HOST = "uz.uzum.app"
const val APPSFLYER_ONELINK_HOST = "uzum.onelink.me"

private val hosts = listOf(
    UZUM_HOST,
    UZUM_WEB_HOST,
    UZUM_HOST_STREAM,
    UZUM_NEW_HOST_STREAM,
)