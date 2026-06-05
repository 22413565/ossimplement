package com.wtorder.app.data.api

import com.wtorder.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface MenuApi {
    @GET("api/menus")
    suspend fun getAllMenus(): Response<List<Menu>>

    @GET("api/menus/{id}")
    suspend fun getMenuById(@Path("id") id: Long): Response<Menu>

    @GET("api/menus/category/{category}")
    suspend fun getMenusByCategory(@Path("category") category: String): Response<List<Menu>>

    @POST("api/menus")
    suspend fun createMenu(@Body menu: Menu): Response<Menu>

    @PUT("api/menus/{id}")
    suspend fun updateMenu(@Path("id") id: Long, @Body request: MenuUpdateRequest): Response<Menu>

    @DELETE("api/menus/{id}")
    suspend fun deleteMenu(@Path("id") id: Long): Response<Void>
}

interface OrderApi {
    @GET("api/orders")
    suspend fun getAllOrders(): Response<List<Order>>

    @GET("api/orders/active")
    suspend fun getActiveOrders(): Response<List<Order>>

    @GET("api/orders/table/{tableNumber}")
    suspend fun getOrdersByTable(@Path("tableNumber") tableNumber: Int): Response<List<Order>>

    @POST("api/orders")
    suspend fun createOrder(@Body request: OrderRequest): Response<Order>

    @POST("api/orders/table-add")
    suspend fun addTableOrder(@Body request: TableOrderAddRequest): Response<Order>

    @DELETE("api/orders/items/{itemId}")
    suspend fun deleteOrderItem(@Path("itemId") itemId: Long): Response<Void>
}

interface TableApi {
    @GET("api/tables")
    suspend fun getAllTables(): Response<List<RestaurantTable>>

    @GET("api/tables/number/{tableNumber}")
    suspend fun getTableByNumber(@Path("tableNumber") tableNumber: Int): Response<RestaurantTable>

    @PUT("api/tables/{id}/status")
    suspend fun updateTableStatus(@Path("id") id: Long, @Query("status") status: String): Response<RestaurantTable>
}

interface StoreApi {
    @GET("api/store")
    suspend fun getStore(): Response<Store>

    @PUT("api/store/{id}")
    suspend fun updateStore(@Path("id") id: Long, @Body request: StoreUpdateRequest): Response<Store>

    @GET("api/store/dashboard")
    suspend fun getDashboard(): Response<DashboardData>
}

interface StaffCallApi {
    @POST("api/staff-call")
    suspend fun callStaff(@Body request: StaffCallRequest): Response<StaffCall>

    @GET("api/staff-call/pending")
    suspend fun getPendingCalls(): Response<List<StaffCall>>

    @PUT("api/staff-call/{id}/acknowledge")
    suspend fun acknowledgeCalled(@Path("id") id: Long): Response<StaffCall>
}
