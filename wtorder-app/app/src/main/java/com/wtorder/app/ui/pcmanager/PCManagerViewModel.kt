package com.wtorder.app.ui.pcmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtorder.app.data.api.RetrofitClient
import com.wtorder.app.data.model.Menu
import com.wtorder.app.data.model.Order
import com.wtorder.app.data.model.RestaurantTable
import com.wtorder.app.data.model.TableOrderAddRequest
import com.wtorder.app.util.WebSocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject

class PCManagerViewModel : ViewModel() {
    private val tableApi = RetrofitClient.tableApi
    private val orderApi = RetrofitClient.orderApi
    private val menuApi = RetrofitClient.menuApi
    private val webSocketManager = WebSocketManager(OkHttpClient())

    private val _tables = MutableStateFlow<List<RestaurantTable>>(emptyList())
    val tables: StateFlow<List<RestaurantTable>> = _tables.asStateFlow()

    private val _currentTableOrders = MutableStateFlow<List<Order>>(emptyList())
    val currentTableOrders: StateFlow<List<Order>> = _currentTableOrders.asStateFlow()

    private val _menus = MutableStateFlow<List<Menu>>(emptyList())
    val menus: StateFlow<List<Menu>> = _menus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTables()
        loadMenus()
        startWebSocket()
    }

    private fun loadTables() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = tableApi.getAllTables()
                if (response.isSuccessful) {
                    _tables.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {} finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTableOrders(tableNumber: Int) {
        viewModelScope.launch {
            try {
                val response = orderApi.getOrdersByTable(tableNumber)
                if (response.isSuccessful) {
                    _currentTableOrders.value = response.body()?.filter { it.status == "CONFIRMED" } ?: emptyList()
                }
            } catch (e: Exception) {}
        }
    }

    private fun loadMenus() {
        viewModelScope.launch {
            try {
                val response = menuApi.getAllMenus()
                if (response.isSuccessful) {
                    _menus.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {}
        }
    }

    private fun startWebSocket() {
        webSocketManager.connect()
        viewModelScope.launch {
            webSocketManager.messages.collect { message ->
                try {
                    val json = JSONObject(message)
                    val type = json.getString("type")
                    if (type == "NEW_ORDER" || type == "ORDER_UPDATED" || type == "STAFF_CALL") {
                        loadTables()
                        // 현재 보고 있는 테이블이 있다면 주문 목록도 갱신
                        // 여기서는 간단히 전체 테이블 정보만 갱신
                    }
                } catch (e: Exception) {}
            }
        }
    }

    fun addMenuItemToTable(tableNumber: Int, menuId: Long, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                val req = TableOrderAddRequest(tableNumber, menuId, quantity)
                orderApi.addTableOrder(req)
                loadTableOrders(tableNumber)
                loadTables() // 상태 업데이트를 위해
            } catch (e: Exception) {}
        }
    }

    fun deleteOrderItem(tableNumber: Int, itemId: Long) {
        viewModelScope.launch {
            try {
                orderApi.deleteOrderItem(itemId)
                loadTableOrders(tableNumber)
                // 만약 모든 주문이 지워졌다면 테이블 상태를 변경해야 할 수도 있지만, 여기서는 생략
            } catch (e: Exception) {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
