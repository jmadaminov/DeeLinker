package com.example.deeplinkapplication

import android.net.Uri
import com.example.deeplinkapplication.deeplink.DeeNode
import com.example.deeplinkapplication.deeplink.DeeSegment
import com.example.deeplinkapplication.deeplink.RootSegments
import com.example.deeplinkapplication.deeplink.constructDeeplinkedNodes


private val deeplinks = listOf(
    "uzum://cabinet",
    "uzum://home",
    "uzum://cabinet/orders",
    "uzum://cabinet/orders/123",
    "uzum://cabinet/orders/all",
//    "uzum://cabinet/home",
)


fun testLinks() {
    deeplinks.forEach {
        if (!testUriConsumable(Uri.parse(it))) throw Exception("URI NOT CONSUMABLE! PLEASE ADD DEEPLINK LOGIC FOR $it")
    }
    println("ALLLL DEEEPLINKS HANDLED!!! ")
}

fun testUriConsumable(uri: Uri): Boolean {
    val deeplinkStartingSegment = constructDeeplinkedNodes(uri)
    return traverseSegments(RootSegments.values().map { it }, deeplinkStartingSegment)
}

fun traverseSegments(actual: List<DeeSegment>, deeNode: DeeNode?): Boolean {
    if (deeNode == null) return false
    actual.forEach {
        if (it.id == deeNode.segment.id) {
            return if (deeNode.next == null) true
            else traverseSegments(it.nextSegments, deeNode.next)
        }
    }
    return false
}