package com.example.deeplinkapplication.deeplink


enum class RootSegments(
    override val id: String,
    override val nextSegments: List<DeeSegment> = listOf()
) : DeeSegment {
    HOME("home"),
    DASHBOARD("dashboard", DashboardSegments.values().toList() + OrdersSegments.values().toList()),
    CABINET("cabinet", CabinetSegments.values().toList())
}

enum class CabinetSegments(
    override val id: String,
    override val nextSegments: List<DeeSegment> = emptyList()
) : DeeSegment {
    ORDERS("orders", OrdersSegments.values().toList()),
    PROFILE("profile");
}

sealed class SSegmentCabinet : SSegment {
    data class Orders(override val name: String = "") : SSegmentCabinet()
}

enum class DashboardSegments(
    override val id: String,
    override val nextSegments: List<DeeSegment> = emptyList()
) : DeeSegment {
    SOME_DASHBOARD_SEGMENT("some_dashboard_segment")
}

enum class OrdersSegments(
    override val id: String,
    override val nextSegments: List<DeeSegment> = listOf()
) : DeeSegment {
    ACTIVE("active"),
    ALL("all"),
    ORDERS("orders")
}