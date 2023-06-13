package com.example.deeplinkapplication.deeplink

import java.io.Serializable

interface DeeNode : Serializable {
    var host: String
    var segment: String
    var nextNode: DeeNode?
    val possibleDirections: MutableList<DeeNode>
    fun getQuery() = params[QUERY_KEY]
    fun getIdParam() = params[PARAM_ID]
    fun setIdParam(id: String?) {
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