package com.wtorder.service

import com.wtorder.model.RestaurantTable
import com.wtorder.repository.TableRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TableService(
    private val tableRepository: TableRepository
) {
    fun getAllTables(): List<RestaurantTable> = tableRepository.findAll()

    fun getTableByNumber(tableNumber: Int): RestaurantTable? =
        tableRepository.findByTableNumber(tableNumber)

    fun updateTableStatus(tableId: Long, status: String): RestaurantTable {
        val table = tableRepository.findById(tableId)
            .orElseThrow { RuntimeException("테이블을 찾을 수 없습니다: $tableId") }
        table.status = status
        return tableRepository.save(table)
    }
}
