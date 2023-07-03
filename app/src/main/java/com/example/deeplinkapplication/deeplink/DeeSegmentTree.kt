package com.example.deeplinkapplication.deeplink

import dev.jmadaminov.deelinker.DeeNode


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

 val hosts = listOf(
    UZUM_HOST,
    UZUM_WEB_HOST,
    UZUM_HOST_STREAM,
    UZUM_NEW_HOST_STREAM,
)



enum class MainDirections(
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