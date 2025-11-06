package com.anshtya.jetx.server.websocket

import com.anshtya.jetx.server.websocket.dto.WebSocketMessageDto
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketHandler : TextWebSocketHandler() {
    private val objectMapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val userSessions = ConcurrentHashMap<UUID, WebSocketSession>()

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage
    ) {
        val payload = message.payload
        val messageMap = objectMapper.readValue(payload, Map::class.java)
        val type = messageMap["type"] as? String ?: return
        val userId = messageMap["userId"] as UUID

        when (type) {
            "subscribe" -> handleSubscribe(session, userId)
            "unsubscribe" -> handleUnsubscribe(session, userId)
            "hello" -> {
                session.sendMessage(TextMessage("hello - ${messageMap["message"]}"))
            }
        }
    }

    override fun afterConnectionEstablished(
        session: WebSocketSession
    ) {
        val userId = session.uri?.query
            ?.split("&")
            ?.find { it.startsWith("userId=") }
            ?.substringAfter("userId=")

        if (userId != null) {
            val key = UUID.fromString(userId)
            userSessions[key] = session
        }
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        userSessions.entries.removeIf { it.value == session }
    }

    fun isUserConnected(
        userId: UUID
    ): Boolean {
        return userSessions.containsKey(userId)
    }

    fun sendToUser(
        userId: UUID,
        data: WebSocketMessageDto
    ) {
        val session = userSessions[userId]

        if (session != null && session.isOpen) {
            session.sendMessage(TextMessage(objectMapper.writeValueAsString(data)))
        }
    }

    fun sendToUsers(
        userIds: List<UUID>,
        data: WebSocketMessageDto
    ) {
        userIds.forEach { id ->
            sendToUser(id, data)
        }
    }

    private fun handleSubscribe(
        session: WebSocketSession,
        userId: UUID
    ) {
        userSessions[userId] = session

        session.sendMessage(TextMessage("Successfully subscribed to userId: $userId"))
    }

    private fun handleUnsubscribe(
        session: WebSocketSession,
        userId: UUID
    ) {
        userSessions.remove(userId)

        session.sendMessage(TextMessage("Successfully unsubscribed from userId: $userId"))
    }
}