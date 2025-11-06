package com.anshtya.jetx.server.messaging.dto

import com.anshtya.jetx.server.messaging.entity.Message
import com.anshtya.jetx.server.messaging.entity.MessageStatus
import com.anshtya.jetx.server.messaging.entity.MessageType
import java.util.*

data class MessageDto(
    val id: UUID,
    val senderId: UUID,
    val targetId: UUID,
    val content: String?,
    val type: MessageType,
    val attachmentId: UUID?,
    val status: MessageStatus?
)

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = id,
        senderId = sender.id!!,
        content = content,
        type = type,
        targetId = receiver?.id ?: group?.id!!,
        attachmentId = attachment?.id,
        status = status
    )
}