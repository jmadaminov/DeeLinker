package dev.jmadaminov.deelinker

import java.io.Serializable

interface DeeNode : Serializable {
    var host: String
    var segment: String
    var nextNode: DeeNode?
    val childNodes: MutableList<DeeNode>
    fun getQuery() = params[QUERY_KEY]
    fun getQueryProperty(key: String): String? {
        val query = params[QUERY_KEY] ?: return null
        val filterRegex = "$key=(?<filter>[A-Za-z\\s]+)".toRegex()
        val match = filterRegex.find(query) ?: return null
        return match.groups[key]?.value
    }

    fun getIdSegment() = params[PARAM_ID]
    fun setIdSegment(id: String?) {
        params[PARAM_ID] = id
    }

    fun setQuery(query: String?) {
        params[QUERY_KEY] = query
    }

    companion object {
        private val params = hashMapOf<String, String?>()
        const val NODE_KEY = "deeplink_node"
        const val PARAM_ID = "deeplink_param_id"
        const val QUERY_KEY = "deeplink_query"
    }
}
