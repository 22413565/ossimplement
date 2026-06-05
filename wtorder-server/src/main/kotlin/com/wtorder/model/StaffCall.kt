package com.wtorder.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "staff_call")
data class StaffCall(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val callId: Long = 0,

    @Column(nullable = false)
    val tableNumber: Int = 0,

    @Column(nullable = false)
    val callTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, length = 20)
    var status: String = "PENDING"  // PENDING, ACKNOWLEDGED
)
