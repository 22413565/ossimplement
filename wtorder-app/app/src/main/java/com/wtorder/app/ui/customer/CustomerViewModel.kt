package com.wtorder.app.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtorder.app.data.api.RetrofitClient
import com.wtorder.app.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerViewModel : ViewModel() {
    private val menuApi = RetrofitClient.menuApi
    private val orderApi = RetrofitClient.orderApi
    private val staffCallApi = RetrofitClient.staffCallApi

    private val _menus = MutableStateFlow<List<Menu>>(emptyList())
    val menus: StateFlow<List<Menu>> = _menus.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _orderSuccess = MutableStateFlow(false)
    val orderSuccess: StateFlow<Boolean> = _orderSuccess.asStateFlow()

    init {
        loadMenus()
    }

    fun loadMenus() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = menuApi.getAllMenus()
                if (response.isSuccessful) {
                    _menus.value = response.body() ?: emptyList()
                } else {
                    _error.value = "메뉴를 불러오는데 실패했습니다."
                }
            } catch (e: Exception) {
                _error.value = "네트워크 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(menu: Menu, quantity: Int = 1) {
        val currentCart = _cart.value.toMutableList()
        val existingItemIndex = currentCart.indexOfFirst { it.menu.menuId == menu.menuId }

        if (existingItemIndex != -1) {
            val existingItem = currentCart[existingItemIndex]
            currentCart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            currentCart.add(CartItem(menu, quantity))
        }
        _cart.value = currentCart
    }

    fun updateCartItemQuantity(menuId: Long, quantity: Int) {
        val currentCart = _cart.value.toMutableList()
        val itemIndex = currentCart.indexOfFirst { it.menu.menuId == menuId }
        
        if (itemIndex != -1) {
            if (quantity <= 0) {
                currentCart.removeAt(itemIndex)
            } else {
                currentCart[itemIndex] = currentCart[itemIndex].copy(quantity = quantity)
            }
            _cart.value = currentCart
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
        _orderSuccess.value = false
    }

    fun placeOrder(tableNumber: Int) {
        if (_cart.value.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val items = _cart.value.map { OrderItemRequest(it.menu.menuId, it.quantity) }
                val request = OrderRequest(tableNumber, items)
                
                val response = orderApi.createOrder(request)
                if (response.isSuccessful) {
                    _orderSuccess.value = true
                    _cart.value = emptyList() // 주문 성공 시 장바구니 비우기
                } else {
                    _error.value = "주문에 실패했습니다."
                }
            } catch (e: Exception) {
                _error.value = "네트워크 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun callStaff(tableNumber: Int) {
        viewModelScope.launch {
            try {
                staffCallApi.callStaff(StaffCallRequest(tableNumber))
            } catch (e: Exception) {
                _error.value = "직원 호출에 실패했습니다."
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
