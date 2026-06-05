package com.wtorder.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.wtorder.dto.WebSocketMessage
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.CopyOnWriteArrayList

@Service
class WebSocketService(
    private val objectMapper: ObjectMapper
) {
    private val sessions = CopyOnWriteArrayList<WebSocketSession>()

    fun addSession(session: WebSocketSession) {
        sessions.add(session)
    }

    fun removeSession(session: WebSocketSession) {
        sessions.remove(session)
    }

    fun sendMessage(message: WebSocketMessage) {
        val json = objectMapper.writeValueAsString(message)
        val textMessage = TextMessage(json)
        sessions.forEach { session ->
            try {
                if (session.isOpen) {
                    session.sendMessage(textMessage)
                }
            } catch (e: Exception) {
                sessions.remove(session)
            }
        }
    }
}
