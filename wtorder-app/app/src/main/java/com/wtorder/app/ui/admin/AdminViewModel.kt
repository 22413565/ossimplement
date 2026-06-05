package com.wtorder.app.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtorder.app.data.api.RetrofitClient
import com.wtorder.app.data.model.DashboardData
import com.wtorder.app.data.model.Store
import com.wtorder.app.data.model.StoreUpdateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val storeApi = RetrofitClient.storeApi

    private val _store = MutableStateFlow<Store?>(null)
    val store: StateFlow<Store?> = _store.asStateFlow()

    private val _dashboard = MutableStateFlow<DashboardData?>(null)
    val dashboard: StateFlow<DashboardData?> = _dashboard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadStoreInfo()
        loadDashboard()
    }

    private fun loadStoreInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = storeApi.getStore()
                if (response.isSuccessful) {
                    _store.value = response.body()
                }
            } catch (e: Exception) {} finally {
                _isLoading.value = false
            }
        }
    }

    fun loadDashboard() {
        viewModelScope.launch {
            try {
                val response = storeApi.getDashboard()
                if (response.isSuccessful) {
                    _dashboard.value = response.body()
                }
            } catch (e: Exception) {}
        }
    }

    fun updateStoreInfo(storeId: Long, name: String, location: String, contact: String, operatingHours: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val req = StoreUpdateRequest(name, location, contact, operatingHours)
                val response = storeApi.updateStore(storeId, req)
                if (response.isSuccessful) {
                    _store.value = response.body()
                }
            } catch (e: Exception) {} finally {
                _isLoading.value = false
            }
        }
    }
}
