package com.wtorder.controller

import com.wtorder.dto.DashboardData
import com.wtorder.dto.StoreUpdateRequest
import com.wtorder.model.Store
import com.wtorder.service.StoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store")
class StoreController(
    private val storeService: StoreService
) {
    @GetMapping
    fun getStore(): ResponseEntity<Store> =
        ResponseEntity.ok(storeService.getStore())

    @PutMapping("/{id}")
    fun updateStore(
        @PathVariable id: Long,
        @RequestBody request: StoreUpdateRequest
    ): ResponseEntity<Store> =
        ResponseEntity.ok(storeService.updateStore(id, request))

    @GetMapping("/dashboard")
    fun getDashboard(): ResponseEntity<DashboardData> =
        ResponseEntity.ok(storeService.getDashboard())
}
