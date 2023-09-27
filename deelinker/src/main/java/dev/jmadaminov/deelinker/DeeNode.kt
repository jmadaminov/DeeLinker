package dev.jmadaminov.deelinker

import java.io.Serializable

interface DeeNode : Serializable {
    var host: String
    var segment: String
    var nextNode: DeeNode?
    val childNodes: MutableList<DeeNode>

    fun getQuery() = metadata[QUERY_KEY]

    fun setQuery(query: String?) {
        metadata[QUERY_KEY] = query
    }

    fun getQueryProperty(key: String): String? {
        val query = metadata[QUERY_KEY] ?: return null
        val filterRegex = "$key=(?<$key>[A-Za-z\\s]+)".toRegex()
        val match = filterRegex.find(query) ?: return null
        return match.groups[key]?.value
    }

    fun cleanMetaData() {
        metadata.clear()
    }

    fun getMetaData(key: String): String? {
        return metadata[key]
    }

    fun setMetaData(key: String, value: String?) {
        metadata[key] = value
    }

    fun getMetaDataMap(): Map<String, String?> = metadata

    companion object {
        fun getMetaData(key: String): String? {
            return metadata[key]
        }

        fun setMetaData(key: String, value: String?) {
            metadata[key] = value
        }

        private val metadata = hashMapOf<String, String?>()
        const val NODE_KEY = "deeplink_node"
        private const val QUERY_KEY = "deeplink_query"
    }
}
