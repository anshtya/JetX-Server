package com.anshtya.jetx.server.messaging.controller

import com.anshtya.jetx.server.messaging.dto.MessageDto
import com.anshtya.jetx.server.messaging.dto.MessageUpdateDto
import com.anshtya.jetx.server.messaging.service.MessageService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/message")
class MessageController(
    private val messageService: MessageService
) {
    @PostMapping("/create")
    fun createMessage(
        @RequestBody messageDto: MessageDto
    ): ResponseEntity<Unit> {
        messageService.saveMessage(messageDto)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getMessage(
        @PathVariable("id") id: UUID
    ): ResponseEntity<MessageDto> {
        val message = messageService.getMessage(id)
        return ResponseEntity.ok(message)
    }

    @PatchMapping("/received/all")
    fun markMessagesReceived(
        request: HttpServletRequest,
        @RequestBody messageUpdateDto: MessageUpdateDto
    ): ResponseEntity<Unit> {
        messageService.updateMessagesReceived(messageUpdateDto)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/received/{id}")
    fun markMessageReceived(
        @PathVariable("id") id: UUID
    ): ResponseEntity<Unit> {
        messageService.updateMessageReceived(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/seen/all")
    fun markMessagesSeen(
        @RequestBody messageUpdateDto: MessageUpdateDto
    ): ResponseEntity<Unit> {
        messageService.updateMessagesSeen(messageUpdateDto)
        return ResponseEntity.noContent().build()
    }
}