package com.wtorder.service

import com.wtorder.dto.*
import com.wtorder.model.Order
import com.wtorder.model.OrderItem
import com.wtorder.repository.MenuRepository
import com.wtorder.repository.OrderItemRepository
import com.wtorder.repository.OrderRepository
import com.wtorder.repository.TableRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val menuRepository: MenuRepository,
    private val tableRepository: TableRepository,
    private val webSocketService: WebSocketService
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun getAllOrders(): List<OrderResponse> =
        orderRepository.findAll().map { toOrderResponse(it) }

    fun getOrdersByTable(tableNumber: Int): List<OrderResponse> =
        orderRepository.findByTableNumber(tableNumber).map { toOrderResponse(it) }

    fun getActiveOrders(): List<OrderResponse> =
        orderRepository.findByStatus("CONFIRMED").map { toOrderResponse(it) }

    fun createOrder(request: OrderRequest): OrderResponse {
        val order = Order(tableNumber = request.tableNumber)
        val savedOrder = orderRepository.save(order)

        request.items.forEach { itemRequest ->
            val menu = menuRepository.findById(itemRequest.menuId)
                .orElseThrow { RuntimeException("메뉴를 찾을 수 없습니다: ${itemRequest.menuId}") }

            if (menu.soldOut) {
                throw RuntimeException("품절된 메뉴입니다: ${menu.name}")
            }

            val orderItem = OrderItem(
                order = savedOrder,
                menu = menu,
                quantity = itemRequest.quantity
            )
            savedOrder.items.add(orderItem)
        }

        val result = orderRepository.save(savedOrder)

        // 테이블 상태 업데이트
        val table = tableRepository.findByTableNumber(request.tableNumber)
        table?.let {
            it.status = "OCCUPIED"
            tableRepository.save(it)
        }

        // WebSocket 알림
        val response = toOrderResponse(result)
        webSocketService.sendMessage(WebSocketMessage("NEW_ORDER", response))

        return response
    }

    fun addItemToTableOrder(request: TableOrderAddRequest): OrderResponse {
        val orders = orderRepository.findByTableNumberAndStatus(request.tableNumber, "CONFIRMED")
        val order = if (orders.isNotEmpty()) {
            orders.last()
        } else {
            val newOrder = Order(tableNumber = request.tableNumber)
            orderRepository.save(newOrder)
        }

        val menu = menuRepository.findById(request.menuId)
            .orElseThrow { RuntimeException("메뉴를 찾을 수 없습니다: ${request.menuId}") }

        val orderItem = OrderItem(
            order = order,
            menu = menu,
            quantity = request.quantity
        )
        order.items.add(orderItem)
        val result = orderRepository.save(order)

        val response = toOrderResponse(result)
        webSocketService.sendMessage(WebSocketMessage("ORDER_UPDATED", response))

        return response
    }

    fun deleteOrderItem(itemId: Long) {
        val item = orderItemRepository.findById(itemId)
            .orElseThrow { RuntimeException("주문 항목을 찾을 수 없습니다: $itemId") }
        orderItemRepository.delete(item)
        webSocketService.sendMessage(WebSocketMessage("ORDER_UPDATED", mapOf("deletedItemId" to itemId)))
    }

    private fun toOrderResponse(order: Order): OrderResponse {
        val items = order.items.map { item ->
            OrderItemResponse(
                itemId = item.itemId,
                menuId = item.menu?.menuId ?: 0,
                menuName = item.menu?.name ?: "",
                price = item.menu?.price ?: 0,
                quantity = item.quantity,
                subtotal = (item.menu?.price ?: 0) * item.quantity
            )
        }
        return OrderResponse(
            orderId = order.orderId,
            tableNumber = order.tableNumber,
            orderTime = order.orderTime.format(formatter),
            status = order.status,
            items = items,
            totalPrice = items.sumOf { it.subtotal }
        )
    }
}
