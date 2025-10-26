package com.anshtya.jetx.server.websocket.dto

data class WebSocketMessageDto(
    val type: WebSocketMessageType,
    val data: Any
)

enum class WebSocketMessageType {
    MESSAGE_UPDATE, PROFILE
}