package com.anshtya.jetx.server.messaging.repository

import com.anshtya.jetx.server.messaging.entity.Message
import com.anshtya.jetx.server.messaging.entity.MessageStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface MessageRepository : JpaRepository<Message, UUID> {
    @Modifying
    @Query("UPDATE Message m SET m.content = null, m.isDeleted = TRUE WHERE m.id = :messageId")
    fun softDeleteMessage(messageId: UUID)

    @Modifying
    @Query("UPDATE Message m SET m.content = null WHERE m.id = :messageId")
    fun softDeleteMessageContent(messageId: UUID)

    @Modifying
    @Query("UPDATE Message m SET m.status = :status WHERE m.id in :messageIds")
    fun updateMessageStatus(messageIds: List<UUID>, status: MessageStatus)
}