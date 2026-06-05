package com.wtorder.service

import com.wtorder.dto.DashboardData
import com.wtorder.dto.StoreUpdateRequest
import com.wtorder.model.Store
import com.wtorder.repository.MenuRepository
import com.wtorder.repository.OrderRepository
import com.wtorder.repository.StoreRepository
import com.wtorder.repository.TableRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository,
    private val orderRepository: OrderRepository,
    private val tableRepository: TableRepository,
    private val menuRepository: MenuRepository
) {
    fun getStore(): Store =
        storeRepository.findAll().firstOrNull() ?: Store(name = "WTOrder 매장")

    fun updateStore(id: Long, request: StoreUpdateRequest): Store {
        val store = storeRepository.findById(id)
            .orElseThrow { RuntimeException("매장을 찾을 수 없습니다: $id") }
        request.name?.let { store.name = it }
        request.location?.let { store.location = it }
        request.contact?.let { store.contact = it }
        request.operatingHours?.let { store.operatingHours = it }
        return storeRepository.save(store)
    }

    fun getDashboard(): DashboardData {
        val orders = orderRepository.findAll()
        val tables = tableRepository.findAll()
        val menus = menuRepository.findAll()
        val activeOrders = orders.filter { it.status == "CONFIRMED" }
        val totalRevenue = orders.flatMap { it.items }.sumOf { (it.menu?.price ?: 0) * it.quantity }

        return DashboardData(
            totalOrders = orders.size,
            totalRevenue = totalRevenue,
            activeOrders = activeOrders.size,
            totalTables = tables.size,
            occupiedTables = tables.count { it.status == "OCCUPIED" },
            totalMenuItems = menus.size
        )
    }
}
