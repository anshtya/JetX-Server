package com.anshtya.jetx.server.messaging.controller

import com.anshtya.jetx.server.messaging.dto.AttachmentCreatedDto
import com.anshtya.jetx.server.messaging.dto.AttachmentDto
import com.anshtya.jetx.server.messaging.service.AttachmentService
import com.anshtya.jetx.server.security.JwtUtil
import com.anshtya.jetx.server.storage.dto.FileUrlDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/attachment")
class AttachmentController(
    private val attachmentService: AttachmentService,
    private val jwtUtil: JwtUtil
) {
    @GetMapping("/upload")
    fun getUploadUrl(
        request: HttpServletRequest,
        @RequestParam("name") name: String,
        @RequestParam("contentType") contentType: String,
    ): ResponseEntity<FileUrlDto> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        val fileUrlDto = attachmentService.getUploadUrl(
            userId = userId,
            fileName = name,
            contentType = contentType
        )
        return ResponseEntity.ok(fileUrlDto)
    }

    @GetMapping("/{id}")
    fun getAttachment(
        @PathVariable("id") id: UUID
    ): ResponseEntity<out Any> {
        val attachment = attachmentService.getAttachment(id)
        return attachment?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build<Unit>()
    }

    @PostMapping("/create")
    fun createAttachment(
        @RequestBody dto: AttachmentDto,
        request: HttpServletRequest
    ): ResponseEntity<AttachmentCreatedDto> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        val attachmentCreatedDto = attachmentService.createAttachment(userId, dto)
        return ResponseEntity.ok(attachmentCreatedDto)
    }
}