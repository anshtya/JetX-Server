package com.anshtya.jetx.server.queue.service

import com.anshtya.jetx.server.messaging.dto.MessageDto
import com.anshtya.jetx.server.messaging.dto.toDto
import com.anshtya.jetx.server.queue.repository.MessageQueueRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class MessageQueueService(
    private val messageQueueRepository: MessageQueueRepository
) {
    fun insertIntoQueue(
        userId: UUID,
        messageId: UUID
    ) {
        messageQueueRepository.saveFromId(
            userId = userId,
            messageId = messageId
        )
    }

    fun multiInsertIntoQueue(
        userIds: List<UUID>,
        messageId: UUID
    ) {
        messageQueueRepository.saveFromIds(
            userIds = userIds,
            messageId = messageId
        )
    }

    fun getUserQueue(
        userId: UUID
    ): List<MessageDto> {
        return messageQueueRepository.findByUserId(userId).map { it.message.toDto() }
    }
}