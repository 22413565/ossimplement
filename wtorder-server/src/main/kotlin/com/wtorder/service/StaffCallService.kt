package com.wtorder.service

import com.wtorder.dto.StaffCallRequest
import com.wtorder.dto.WebSocketMessage
import com.wtorder.model.StaffCall
import com.wtorder.repository.StaffCallRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StaffCallService(
    private val staffCallRepository: StaffCallRepository,
    private val webSocketService: WebSocketService
) {
    fun callStaff(request: StaffCallRequest): StaffCall {
        val call = StaffCall(tableNumber = request.tableNumber)
        val saved = staffCallRepository.save(call)
        webSocketService.sendMessage(WebSocketMessage("STAFF_CALL", saved))
        return saved
    }

    fun getPendingCalls(): List<StaffCall> =
        staffCallRepository.findByStatus("PENDING")

    fun acknowledgeCalled(callId: Long): StaffCall {
        val call = staffCallRepository.findById(callId)
            .orElseThrow { RuntimeException("호출을 찾을 수 없습니다: $callId") }
        call.status = "ACKNOWLEDGED"
        return staffCallRepository.save(call)
    }
}
