@file:OptIn(ExperimentalStdlibApi::class)

package com.example.deeplinkapplication.deeplink

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class DeeNode(
    val segment: DeeSegment,
    var next: DeeNode? = null,
    val params: @RawValue HashMap<String, String?> = hashMapOf(),
    val host: String? = null,
) : Parcelable {

    companion object {
        const val NODE_KEY = "deeplink_node"
        const val PARAM_ID = "deeplink_param_id"
        const val QUERY_KEY = "deeplink_query"
    }

//    override fun equals(other: Any?): Boolean {
//        return when (other) {
//            is DeeplinkNode -> this.segment == other.segment
//            else -> false
//        }
//    }
//
//    override fun toString(): String {
//        return segment
//    }
//
//    override fun hashCode(): Int {
//        var result = segment.hashCode()
//        result = 31 * result + (next?.hashCode() ?: 0)
//        return result
//    }

    fun addIdParam(id: String) {
        params[PARAM_ID] = id
    }

    fun getIdParam() = params[PARAM_ID]

    fun getQuery() = params[QUERY_KEY]
    fun setQuery(query: String?) {
        params[QUERY_KEY] = query
    }
}


