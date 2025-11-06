package com.anshtya.jetx.server.queue.controller

import com.anshtya.jetx.server.messaging.dto.MessageDto
import com.anshtya.jetx.server.queue.service.MessageQueueService
import com.anshtya.jetx.server.security.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/queue")
class MessageQueueController(
    private val messageQueueService: MessageQueueService,
    private val jwtUtil: JwtUtil
) {
    @GetMapping("/pending")
    fun getPendingMessages(
        request: HttpServletRequest
    ): ResponseEntity<List<MessageDto>> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        val messages = messageQueueService.getUserQueue(userId)
        return ResponseEntity.ok(messages)
    }
}