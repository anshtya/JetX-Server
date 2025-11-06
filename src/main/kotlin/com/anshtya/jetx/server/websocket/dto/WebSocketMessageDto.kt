package com.anshtya.jetx.server.websocket.dto

data class WebSocketMessageDto(
    val type: WebSocketMessageType,
    val data: Any
)

enum class WebSocketMessageType {
    NEW_MESSAGE, MESSAGE_UPDATE, PROFILE
}