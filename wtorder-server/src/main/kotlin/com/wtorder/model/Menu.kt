package com.wtorder.model

import jakarta.persistence.*

@Entity
@Table(name = "menu")
data class Menu(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val menuId: Long = 0,

    @Column(nullable = false, length = 100)
    var name: String = "",

    @Column(nullable = false)
    var price: Int = 0,

    @Column(columnDefinition = "TEXT")
    var description: String = "",

    @Column(length = 500)
    var imagePath: String = "",

    @Column(length = 50)
    var category: String = "",

    @Column(nullable = false)
    var soldOut: Boolean = false
)
