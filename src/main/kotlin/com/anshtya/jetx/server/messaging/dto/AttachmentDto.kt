package com.anshtya.jetx.server.messaging.dto

import com.anshtya.jetx.server.messaging.entity.Attachment

data class AttachmentDto(
    val name: String,
    val type: String,
    val width: Int,
    val height: Int
)

fun Attachment.toDto(): AttachmentDto {
    return AttachmentDto(
        name = name,
        type = type,
        width = width,
        height = height,
    )
}

fun AttachmentDto.toEntity(fileName: String): Attachment {
    return Attachment(
        name = fileName,
        type = type,
        width = width,
        height = height,
    )
}
