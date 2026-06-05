package com.wtorder.repository

import com.wtorder.model.Menu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository : JpaRepository<Menu, Long> {
    fun findByCategory(category: String): List<Menu>
    fun findBySoldOut(soldOut: Boolean): List<Menu>
}
