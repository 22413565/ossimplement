package com.wtorder.controller

import com.wtorder.dto.StaffCallRequest
import com.wtorder.model.StaffCall
import com.wtorder.service.StaffCallService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/staff-call")
class StaffCallController(
    private val staffCallService: StaffCallService
) {
    @PostMapping
    fun callStaff(@RequestBody request: StaffCallRequest): ResponseEntity<StaffCall> =
        ResponseEntity.ok(staffCallService.callStaff(request))

    @GetMapping("/pending")
    fun getPendingCalls(): ResponseEntity<List<StaffCall>> =
        ResponseEntity.ok(staffCallService.getPendingCalls())

    @PutMapping("/{id}/acknowledge")
    fun acknowledgeCalled(@PathVariable id: Long): ResponseEntity<StaffCall> =
        ResponseEntity.ok(staffCallService.acknowledgeCalled(id))
}
