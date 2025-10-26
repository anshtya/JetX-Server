package com.anshtya.jetx.server.websocket.dto

import java.util.*

data class MessageStatusUpdate(
    val id: UUID,
    val received: Boolean,
    val seen: Boolean
)
