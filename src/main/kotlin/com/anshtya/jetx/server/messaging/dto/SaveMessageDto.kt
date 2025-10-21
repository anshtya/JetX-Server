package com.anshtya.jetx.server.messaging.dto

import com.anshtya.jetx.server.messaging.entity.MessageType
import java.util.*

data class SaveMessageDto(
    val senderId: UUID,
    val targetId: UUID,
    val content: String?,
    val type: MessageType
)