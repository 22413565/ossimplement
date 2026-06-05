package com.wtorder.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderId: Long = 0,

    @Column(nullable = false)
    val tableNumber: Int = 0,

    @Column(nullable = false)
    val orderTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, length = 20)
    var status: String = "CONFIRMED",

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    val items: MutableList<OrderItem> = mutableListOf()
)
