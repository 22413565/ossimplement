package com.wtorder.app.ui.kitchen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtorder.app.data.api.RetrofitClient
import com.wtorder.app.data.model.Order
import com.wtorder.app.util.WebSocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject

class KitchenViewModel : ViewModel() {
    private val orderApi = RetrofitClient.orderApi
    
    // WebSocket 클라이언트는 싱글톤 또는 의존성 주입으로 관리하는 것이 좋으나, 여기서는 ViewModel에 종속적으로 생성
    private val webSocketManager = WebSocketManager(OkHttpClient())

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadActiveOrders()
        startWebSocket()
    }

    private fun loadActiveOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = orderApi.getActiveOrders()
                if (response.isSuccessful) {
                    _orders.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
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
                        // 새 주문이나 수정 알림이 오면 목록 다시 로드
                        loadActiveOrders()
                    }
                } catch (e: Exception) {
                    // JSON parsing error
                }
            }
        }
    }

    fun completeOrder(orderId: Long) {
        // 주방에서 조리 완료 처리하는 API 호출 (현재 백엔드에는 상태 변경 API가 미구현이므로 목록 갱신만 수행하는 것으로 시뮬레이션)
        // 실제로는 PUT /api/orders/{id}/status 와 같은 API 필요
        val currentOrders = _orders.value.toMutableList()
        currentOrders.removeAll { it.orderId == orderId }
        _orders.value = currentOrders
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
