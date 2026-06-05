package com.wtorder.dto

data class OrderRequest(
    val tableNumber: Int,
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val menuId: Long,
    val quantity: Int
)

data class OrderResponse(
    val orderId: Long,
    val tableNumber: Int,
    val orderTime: String,
    val status: String,
    val items: List<OrderItemResponse>,
    val totalPrice: Int
)

data class OrderItemResponse(
    val itemId: Long,
    val menuId: Long,
    val menuName: String,
    val price: Int,
    val quantity: Int,
    val subtotal: Int
)

data class StaffCallRequest(
    val tableNumber: Int
)

data class TableOrderAddRequest(
    val tableNumber: Int,
    val menuId: Long,
    val quantity: Int
)

data class StoreUpdateRequest(
    val name: String?,
    val location: String?,
    val contact: String?,
    val operatingHours: String?
)

data class MenuUpdateRequest(
    val name: String?,
    val price: Int?,
    val description: String?,
    val category: String?,
    val soldOut: Boolean?
)

data class WebSocketMessage(
    val type: String,  // NEW_ORDER, ORDER_DELETED, STAFF_CALL, ORDER_UPDATED
    val data: Any
)

data class DashboardData(
    val totalOrders: Int,
    val totalRevenue: Int,
    val activeOrders: Int,
    val totalTables: Int,
    val occupiedTables: Int,
    val totalMenuItems: Int
)
