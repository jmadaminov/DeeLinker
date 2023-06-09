@file:OptIn(ExperimentalStdlibApi::class)

package com.example.deeplinkapplication.deeplink

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController


inline fun <reified T> Fragment.deeplinkInto(
    node: DeeNode?,
    setupIntent: context(Intent)() -> Unit
) {
    startActivity(Intent(requireActivity(), T::class.java).apply {
        setupIntent(this)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        putExtra(DeeNode.NODE_KEY, node)
    })
}

inline fun <reified T> Activity.deeplinkInto(
    node: DeeNode?,
    setupIntent: context(Intent)() -> Unit
) {
    startActivity(Intent(this, T::class.java).apply {
        setupIntent(this)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        putExtra(DeeNode.NODE_KEY, node)
    })
}

inline fun <reified T> Activity.deeplinkInto(node: DeeNode?) {
    startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        putExtra(DeeNode.NODE_KEY, node)
    })
}

fun NavController.deeplinkInto(@IdRes destinationId: Int, node: DeeNode?) {
    navigate(destinationId, Bundle().apply { putParcelable(DeeNode.NODE_KEY, node) })
}

//fun Activity.consumeDeeNode(): DeeNode? {
//    val deeNode: DeeNode? = intent.extras?.getParcelable(DeeNode.NODE_KEY)
//    intent.extras?.putParcelable(DeeNode.NODE_KEY, null)
//    return deeNode
//}


inline fun <reified E : Enum<E>> Activity.consumeDeeNodeAs(block: (E?, DeeNode) -> Unit) {
    val deeNode: DeeNode? = intent.extras?.getParcelable(DeeNode.NODE_KEY)
    intent.extras?.putParcelable(DeeNode.NODE_KEY, null)
    deeNode?.let {
        block(
            enumValues<E>().find { (it as DeeSegment).id == deeNode.segment.id },
            deeNode
        )
    }
}

inline fun <reified E : Enum<E>> Fragment.consumeDlNodeInFragAs(block: (E?, DeeNode) -> Unit) {
    val deeNode: DeeNode? = arguments?.getParcelable(DeeNode.NODE_KEY)
    arguments?.putParcelable(DeeNode.NODE_KEY, null)
    deeNode?.let {
        block(
            enumValues<E>().find { (it as com.example.deeplinkapplication.deeplink.DeeSegment).id == deeNode.segment.id },
            deeNode
        )
    }
}


fun constructDeeplinkedNodes(deeplinkUri: Uri, vararg deeManuals: DeeManual): DeeNode? {
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
            currentNode = DeeNode(host = "", segment = object : DeeSegment {
                override val id: String
                    get() = host
            })
        }
    }

    deeplinkUri.path?.split("/")?.forEach lit@{ pathEntry ->
        if (listOf("ru", "uz", "en").contains(pathEntry)) return@lit

        pathEntry.toLongOrNull()?.let {
            currentNode?.addIdParam(pathEntry)
            return@lit
        }

        if (pathEntry.isNotBlank()) {
            val temp = DeeNode(host = deeplinkUri.host, segment = object : DeeSegment {
                override val id: String
                    get() = pathEntry
            })
            currentNode?.next = temp
            currentNode = temp
        }
        if (start == null) start = currentNode
        currentNode?.setQuery(deeplinkUri.query)
    }
    return start
}


//uzum://user/settings
//https://uzum.uz/user/order/400/feedback

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


//"uzum://"
//"uzum://cart"
//"uzum://wishes"
//"uzum://faq"
//"uzum://category/10020"
//"https://uzum.uz/category/10020
//"https://uzum.uz/uz/category/10020
//"https://uzum.uz/ru/category/10020
//"uzum://user"
//"uzum://user/settings"
//"uzum://user/reviews"
//"uzum://user/chats"
//"uzum://user/notifications"
//"uzum://user/promocodes"
//"uzum://user/orders?filter=current"
//"uzum://user/orders?filter=all"
//"uzum://user/orders?filter=unfinished"
//"uzum://user/orders?filter=current"
//"uzum://search"
//"uzum://search?query=xiaomi"
//"uzum://product/39467"
//"uzum://bitovayahimiyaikosmetikapouhodun1"
//"uzum://about/delivery-points"


//class TypeChecker<T : Any>(val klass: Class<T>) {
//    companion object {
//        inline operator fun <reified T : Any> invoke() = TypeChecker(T::class.java)
//    }
//
//    fun isSameType(t: Any): Boolean {
//        return when {
//            klass.isAssignableFrom(t.javaClass) -> true
//            else -> false
//        }
//    }
//}
