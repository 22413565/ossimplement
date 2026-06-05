package com.wtorder.websocket

import com.wtorder.service.WebSocketService
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class OrderWebSocketHandler(
    private val webSocketService: WebSocketService
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        webSocketService.addSession(session)
        println("WebSocket 연결됨: ${session.id}")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        // 클라이언트에서 메시지를 보내면 여기서 처리
        println("메시지 수신: ${message.payload}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        webSocketService.removeSession(session)
        println("WebSocket 연결 해제: ${session.id}")
    }
}
