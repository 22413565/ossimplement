package com.wtorder.repository

import com.wtorder.model.StaffCall
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StaffCallRepository : JpaRepository<StaffCall, Long> {
    fun findByStatus(status: String): List<StaffCall>
    fun findByTableNumber(tableNumber: Int): List<StaffCall>
}
