package com.anshtya.jetx.server.websocket

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

        when (type) {
            "subscribe" -> handleSubscribe(session, messageMap)
            "unsubscribe" -> handleUnsubscribe(session, messageMap)
            "hello" -> {
                session.sendMessage(TextMessage("hello - ${messageMap["message"]}"))
            }
        }
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        userSessions.entries.removeIf { it.value == session }
    }

    fun sendDbUpdateToUser(userId: UUID, data: Map<String, Any>) {
        val session = userSessions[userId]
    }

    private fun handleSubscribe(
        session: WebSocketSession,
        messageMap: Map<*, *>
    ) {
        val userId = messageMap["userId"] as UUID

        userSessions[userId] = session

        session.sendMessage(TextMessage("Successfully subscribed to userId: $userId"))
    }

    private fun handleUnsubscribe(
        session: WebSocketSession,
        messageMap: Map<*, *>
    ) {
        val userId = messageMap["userId"] as UUID

        userSessions.remove(userId)

        session.sendMessage(TextMessage("Successfully unsubscribed from userId: $userId"))
    }
}