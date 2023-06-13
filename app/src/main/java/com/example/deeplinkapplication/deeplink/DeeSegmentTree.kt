package com.example.deeplinkapplication.deeplink

enum class RootDirections(
    override var segment: String,
    override val possibleDirections: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    HOME("home"),
    DASHBOARD("dashboard") {
        override val possibleDirections = mutableListOf<DeeNode>(
            *DashboardDirections.values(),
            *OrdersDirections.values()
        )
    },
    CABINET("cabinet") {
        override val possibleDirections: MutableList<DeeNode> =
            mutableListOf(*CabinetDirections.values())
    }
}

enum class CabinetDirections(
    override var segment: String,
    override val possibleDirections: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    ORDERS("orders") {
        override val possibleDirections = mutableListOf<DeeNode>(*OrdersDirections.values())
    },
    PROFILE("profile") {
        override val possibleDirections = mutableListOf<DeeNode>()
    }
}

enum class DashboardDirections(
    override var segment: String,
    override val possibleDirections: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    SOME_DASHBOARD_NODE("some_dashboard_segment"),
}

enum class OrdersDirections(
    override var segment: String,
    override val possibleDirections: MutableList<DeeNode> = mutableListOf(),
    override var host: String = "",
    override var nextNode: DeeNode? = null,
) : DeeNode {
    ACTIVE("active"),
    ALL("all"),
}