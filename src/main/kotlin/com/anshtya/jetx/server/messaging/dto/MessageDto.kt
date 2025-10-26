package com.anshtya.jetx.server.messaging.dto

import com.anshtya.jetx.server.messaging.entity.Message
import com.anshtya.jetx.server.messaging.entity.MessageType
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

data class MessageDto(
    val id: UUID,
    val senderId: UUID,
    val targetId: UUID,
    val content: String?,
    val type: MessageType,
    val attachmentId: UUID?
)

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = id,
        senderId = sender.id!!,
        content = content,
        type = type,
        targetId = receiver?.id ?: group?.id!!,
        attachmentId = attachment?.id
    )
}

fun MessageDto.toStringMap(): Map<String, String> {
    return (this::class as KClass<MessageDto>).memberProperties.associate { property ->
        property.name to property.get(this).toString()
    }.toMap()
}