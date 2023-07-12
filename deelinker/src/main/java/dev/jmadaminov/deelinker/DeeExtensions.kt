package dev.jmadaminov.deelinker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController

inline fun <reified T> Fragment.deeLinkInto(
    node: DeeNode,
    setupIntent: context(Intent)() -> Unit
) {
    startActivity(Intent(requireActivity(), T::class.java).apply {
        setupIntent(this)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        putExtra(DeeNode.QUERY_KEY, node.getQuery())
        putExtra(DeeNode.PARAM_ID, node.getIdSegment())
        putExtra(DeeNode.NODE_KEY, node.nextNode)
    })
}

inline fun <reified T> Fragment.deeLinkInto(node: DeeNode) {
    startActivity(Intent(requireActivity(), T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        putExtra(DeeNode.QUERY_KEY, node.getQuery())
        putExtra(DeeNode.PARAM_ID, node.getIdSegment())
        putExtra(DeeNode.NODE_KEY, node.nextNode)
    })
}

inline fun <reified T> Activity.deeLinkInto(
    node: DeeNode?,
    setupIntent: context(Intent)() -> Unit = {}
) {
    startActivity(Intent(this, T::class.java).apply {
        setupIntent(this)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        putExtra(DeeNode.NODE_KEY, node)
    })
}

fun NavController.deeLinkInto(@IdRes destinationId: Int, node: DeeNode?) {
    navigate(
        destinationId,
        Bundle().apply { putSerializable(DeeNode.NODE_KEY, node) })
}


inline fun <reified E : Enum<E>> Activity.consumeDeeNodeAs(
    onParamId: (String, DeeNode?) -> Unit = { _, _ -> },
    onQuery: (String, DeeNode?) -> Unit = { _, _ -> },
    consumeBlock: (E, DeeNode) -> Unit
) {
    val deeNode = intent.extras?.getSerializable(DeeNode.NODE_KEY) as DeeNode?
    intent.extras?.getString(DeeNode.PARAM_ID)?.let { onParamId(it, deeNode) }
    intent.extras?.getString(DeeNode.QUERY_KEY)?.let { onQuery(it, deeNode) }
    intent.extras?.putSerializable(DeeNode.NODE_KEY, null)
    deeNode?.let {
        val enumValue = enumValues<E>().find { (it as DeeNode).segment == deeNode.segment }
        if (enumValue == null) {
            val dirs =
                E::class.simpleName + " [" + E::class.java.enumConstants?.joinToString { it.name } + "]"

            Log.w(
                "DeeLinker",
                "Trying to consume unknown node ==> ${deeNode.segment} <== which does not exist in *** $dirs ***. Make sure you have registered the node in DeeSegmentTree.kt"
            )
        } else {
            consumeBlock(enumValue, deeNode)
        }
    }
}

inline fun <reified E : Enum<E>> Fragment.consumeDeeNodeInFragAs(block: (E, DeeNode) -> Unit) {
    val deeNode = arguments?.getSerializable(DeeNode.NODE_KEY) as DeeNode?
    arguments?.putSerializable(DeeNode.NODE_KEY, null)
    deeNode?.let {
        val enumValue = enumValues<E>().find { (it as DeeNode).segment == deeNode.segment }
        if (enumValue == null) {
            val dirs =
                E::class.simpleName + " [" + E::class.java.enumConstants?.joinToString { it.name } + "]"

            Log.w(
                "DeeLinker",
                "Trying to consume unknown node ==> ${deeNode.segment} <== which does not exist in *** $dirs ***. Make sure you have registered the node in DeeSegmentTree.kt"
            )
        } else {
            block(enumValue, deeNode)
        }
    }
}

inline fun deeConfig(setup: DeeLinkerConfig.() -> Unit): DeeLinkerConfig {
    return DeeLinkerConfig().apply {
        setup()
    }
}
