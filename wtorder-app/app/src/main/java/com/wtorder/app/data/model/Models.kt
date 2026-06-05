package com.wtorder.app.data.model

data class Menu(
    val menuId: Long = 0,
    val name: String = "",
    val price: Int = 0,
    val description: String = "",
    val imagePath: String = "",
    val category: String = "",
    val soldOut: Boolean = false
)

data class Order(
    val orderId: Long = 0,
    val tableNumber: Int = 0,
    val orderTime: String = "",
    val status: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Int = 0
)

data class OrderItem(
    val itemId: Long = 0,
    val menuId: Long = 0,
    val menuName: String = "",
    val price: Int = 0,
    val quantity: Int = 0,
    val subtotal: Int = 0
)

data class RestaurantTable(
    val tableId: Long = 0,
    val tableNumber: Int = 0,
    val status: String = "EMPTY"
)

data class Store(
    val storeId: Long = 0,
    val name: String = "",
    val location: String = "",
    val contact: String = "",
    val operatingHours: String = ""
)

data class StaffCall(
    val callId: Long = 0,
    val tableNumber: Int = 0,
    val callTime: String = "",
    val status: String = ""
)

data class DashboardData(
    val totalOrders: Int = 0,
    val totalRevenue: Int = 0,
    val activeOrders: Int = 0,
    val totalTables: Int = 0,
    val occupiedTables: Int = 0,
    val totalMenuItems: Int = 0
)

// Request DTOs
data class OrderRequest(
    val tableNumber: Int,
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val menuId: Long,
    val quantity: Int
)

data class TableOrderAddRequest(
    val tableNumber: Int,
    val menuId: Long,
    val quantity: Int
)

data class StaffCallRequest(
    val tableNumber: Int
)

data class StoreUpdateRequest(
    val name: String? = null,
    val location: String? = null,
    val contact: String? = null,
    val operatingHours: String? = null
)

data class MenuUpdateRequest(
    val name: String? = null,
    val price: Int? = null,
    val description: String? = null,
    val category: String? = null,
    val soldOut: Boolean? = null
)

// Cart item (client-side only)
data class CartItem(
    val menu: Menu,
    val quantity: Int
)
