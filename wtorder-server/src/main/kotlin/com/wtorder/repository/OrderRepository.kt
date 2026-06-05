package com.wtorder.repository

import com.wtorder.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByTableNumber(tableNumber: Int): List<Order>
    fun findByStatus(status: String): List<Order>
    fun findByTableNumberAndStatus(tableNumber: Int, status: String): List<Order>
}
