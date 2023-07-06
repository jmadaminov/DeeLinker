package com.example.deeplinkapplication.deeplink

import dev.jmadaminov.deelinker.DeeNode

private const val DOMAIN_HOST = "domain.uz"
private const val DOMAIN_WEB_HOST = "www.domain.uz"

val myHosts = listOf(
    DOMAIN_HOST,
    DOMAIN_WEB_HOST,
)

enum class MainDirections(
    override var segment: String,
    override val childNodes: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    HOME("home"),
    DASHBOARD("dashboard") {
        override val childNodes = mutableListOf<DeeNode>(
            *DashboardDirections.values(),
            *OrdersDirections.values()
        )
    },
    CABINET("cabinet") {
        override val childNodes: MutableList<DeeNode> =
            mutableListOf(*CabinetDirections.values())
    }
}

enum class CabinetDirections(
    override var segment: String,
    override val childNodes: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    ORDERS("orders") {
        override val childNodes = mutableListOf<DeeNode>(*OrdersDirections.values())
    },
    PROFILE("profile") {
        override val childNodes = mutableListOf<DeeNode>()
    }
}

enum class DashboardDirections(
    override var segment: String,
    override val childNodes: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    SOME_DASHBOARD_NODE("some_dashboard_segment"),
}

enum class OrdersDirections(
    override var segment: String,
    override val childNodes: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    ACTIVE("active"),
    ALL("all"),
}