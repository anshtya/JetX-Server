package com.anshtya.jetx.server.messaging.service

import com.anshtya.jetx.server.messaging.dto.AttachmentCreatedDto
import com.anshtya.jetx.server.messaging.dto.AttachmentDto
import com.anshtya.jetx.server.messaging.dto.toDto
import com.anshtya.jetx.server.messaging.dto.toEntity
import com.anshtya.jetx.server.messaging.repository.AttachmentRepository
import com.anshtya.jetx.server.storage.dto.FileUrlDto
import com.anshtya.jetx.server.storage.service.FileStorageService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class AttachmentService(
    private val repository: AttachmentRepository,
    private val storageService: FileStorageService
) {
    fun getUploadUrl(
        userId: UUID,
        fileName: String,
        contentType: String
    ): FileUrlDto {
        return storageService.generateUploadUrl(
            generateAttachmentName(fileName, userId),
            contentType
        )
    }

    fun createAttachment(
        userId: UUID,
        attachmentDto: AttachmentDto
    ): AttachmentCreatedDto {
        val fileName = generateAttachmentName(attachmentDto.name, userId)
        val attachment = repository.save(attachmentDto.toEntity(fileName))
        return AttachmentCreatedDto(attachment.id!!)
    }

    fun getAttachment(
        id: UUID
    ): AttachmentDto? {
        return repository.findById(id).getOrElse {
            return@getOrElse null
        }?.toDto()
    }

    private fun generateAttachmentName(
        fileName: String,
        userId: UUID
    ): String {
        return "$fileName-$userId"
    }
}