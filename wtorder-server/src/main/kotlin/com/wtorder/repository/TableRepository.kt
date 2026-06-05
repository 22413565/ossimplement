package com.wtorder.repository

import com.wtorder.model.RestaurantTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TableRepository : JpaRepository<RestaurantTable, Long> {
    fun findByTableNumber(tableNumber: Int): RestaurantTable?
    fun findByStatus(status: String): List<RestaurantTable>
}
