package com.wtorder.app.ui.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtorder.app.data.api.RetrofitClient
import com.wtorder.app.data.model.Menu
import com.wtorder.app.data.model.MenuUpdateRequest
import com.wtorder.app.data.model.Order
import com.wtorder.app.data.model.StaffCall
import com.wtorder.app.util.WebSocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject

class StoreViewModel : ViewModel() {
    private val menuApi = RetrofitClient.menuApi
    private val orderApi = RetrofitClient.orderApi
    private val staffCallApi = RetrofitClient.staffCallApi
    private val webSocketManager = WebSocketManager(OkHttpClient())

    private val _menus = MutableStateFlow<List<Menu>>(emptyList())
    val menus: StateFlow<List<Menu>> = _menus.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _staffCalls = MutableStateFlow<List<StaffCall>>(emptyList())
    val staffCalls: StateFlow<List<StaffCall>> = _staffCalls.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMenus()
        loadOrders()
        loadStaffCalls()
        startWebSocket()
    }

    fun loadMenus() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = menuApi.getAllMenus()
                if (response.isSuccessful) {
                    _menus.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            try {
                val response = orderApi.getActiveOrders()
                if (response.isSuccessful) {
                    _orders.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {}
        }
    }

    private fun loadStaffCalls() {
        viewModelScope.launch {
            try {
                val response = staffCallApi.getPendingCalls()
                if (response.isSuccessful) {
                    _staffCalls.value = response.body() ?: emptyList()
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
                    if (type == "NEW_ORDER" || type == "ORDER_UPDATED") {
                        loadOrders()
                    } else if (type == "STAFF_CALL") {
                        loadStaffCalls()
                    }
                } catch (e: Exception) {}
            }
        }
    }

    fun saveMenu(menuId: Long, name: String, price: Int, description: String, category: String, soldOut: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (menuId == 0L) {
                    val newMenu = Menu(name = name, price = price, description = description, category = category, soldOut = soldOut)
                    menuApi.createMenu(newMenu)
                } else {
                    val updateReq = MenuUpdateRequest(name, price, description, category, soldOut)
                    menuApi.updateMenu(menuId, updateReq)
                }
                loadMenus()
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMenu(menuId: Long) {
        viewModelScope.launch {
            try {
                menuApi.deleteMenu(menuId)
                loadMenus()
            } catch (e: Exception) {}
        }
    }

    fun toggleSoldOut(menu: Menu) {
        viewModelScope.launch {
            try {
                val updateReq = MenuUpdateRequest(soldOut = !menu.soldOut)
                menuApi.updateMenu(menu.menuId, updateReq)
                loadMenus()
            } catch (e: Exception) {}
        }
    }

    fun acknowledgeStaffCall(callId: Long) {
        viewModelScope.launch {
            try {
                staffCallApi.acknowledgeCalled(callId)
                loadStaffCalls()
            } catch (e: Exception) {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
