package com.wtorder.model

import jakarta.persistence.*

@Entity
@Table(name = "store")
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val storeId: Long = 0,

    @Column(nullable = false, length = 100)
    var name: String = "",

    @Column(length = 200)
    var location: String = "",

    @Column(length = 50)
    var contact: String = "",

    @Column(length = 100)
    var operatingHours: String = ""
)
