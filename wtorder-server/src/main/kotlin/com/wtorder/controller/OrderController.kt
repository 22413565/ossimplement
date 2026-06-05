package com.wtorder.controller

import com.wtorder.dto.OrderRequest
import com.wtorder.dto.OrderResponse
import com.wtorder.dto.TableOrderAddRequest
import com.wtorder.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping
    fun getAllOrders(): ResponseEntity<List<OrderResponse>> =
        ResponseEntity.ok(orderService.getAllOrders())

    @GetMapping("/active")
    fun getActiveOrders(): ResponseEntity<List<OrderResponse>> =
        ResponseEntity.ok(orderService.getActiveOrders())

    @GetMapping("/table/{tableNumber}")
    fun getOrdersByTable(@PathVariable tableNumber: Int): ResponseEntity<List<OrderResponse>> =
        ResponseEntity.ok(orderService.getOrdersByTable(tableNumber))

    @PostMapping
    fun createOrder(@RequestBody request: OrderRequest): ResponseEntity<OrderResponse> =
        ResponseEntity.ok(orderService.createOrder(request))

    @PostMapping("/table-add")
    fun addTableOrder(@RequestBody request: TableOrderAddRequest): ResponseEntity<OrderResponse> =
        ResponseEntity.ok(orderService.addItemToTableOrder(request))

    @DeleteMapping("/items/{itemId}")
    fun deleteOrderItem(@PathVariable itemId: Long): ResponseEntity<Void> {
        orderService.deleteOrderItem(itemId)
        return ResponseEntity.noContent().build()
    }
}
