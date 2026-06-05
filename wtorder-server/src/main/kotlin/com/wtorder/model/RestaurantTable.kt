package com.wtorder.model

import jakarta.persistence.*

@Entity
@Table(name = "restaurant_table")
data class RestaurantTable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tableId: Long = 0,

    @Column(nullable = false, unique = true)
    val tableNumber: Int = 0,

    @Column(nullable = false, length = 20)
    var status: String = "EMPTY"  // EMPTY, OCCUPIED, NEEDS_SERVICE
)
