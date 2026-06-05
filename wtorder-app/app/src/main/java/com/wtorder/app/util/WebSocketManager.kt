package com.wtorder.app.util

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import org.json.JSONObject

class WebSocketManager(private val client: OkHttpClient) {
    private var webSocket: WebSocket? = null
    private val _messages = MutableSharedFlow<String>(replay = 1)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    // 에뮬레이터 로컬 서버 주소
    private val WS_URL = "ws://10.0.2.2:8080/ws/orders"

    fun connect() {
        if (webSocket != null) return
        
        val request = Request.Builder().url(WS_URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocketManager", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocketManager", "Message received: $text")
                _messages.tryEmit(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocketManager", "Error: ${t.message}")
                this@WebSocketManager.webSocket = null
                // 에러 발생 시 재연결 시도할 수 있음
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocketManager", "Closed: $reason")
                this@WebSocketManager.webSocket = null
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
    }
}
