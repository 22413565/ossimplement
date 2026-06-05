package com.wtorder.controller

import com.wtorder.model.RestaurantTable
import com.wtorder.service.TableService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tables")
class TableController(
    private val tableService: TableService
) {
    @GetMapping
    fun getAllTables(): ResponseEntity<List<RestaurantTable>> =
        ResponseEntity.ok(tableService.getAllTables())

    @GetMapping("/number/{tableNumber}")
    fun getTableByNumber(@PathVariable tableNumber: Int): ResponseEntity<RestaurantTable> {
        val table = tableService.getTableByNumber(tableNumber)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(table)
    }

    @PutMapping("/{id}/status")
    fun updateTableStatus(
        @PathVariable id: Long,
        @RequestParam status: String
    ): ResponseEntity<RestaurantTable> =
        ResponseEntity.ok(tableService.updateTableStatus(id, status))
}
