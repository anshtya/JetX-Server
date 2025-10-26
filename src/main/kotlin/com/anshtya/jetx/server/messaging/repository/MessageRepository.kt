package com.anshtya.jetx.server.messaging.repository

import com.anshtya.jetx.server.messaging.entity.Message
import com.anshtya.jetx.server.messaging.entity.MessageStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface MessageRepository : JpaRepository<Message, UUID> {
    @Modifying
    @Query("UPDATE Message m SET m.status = :status, m.content = null WHERE m.id in :messageIds")
    fun updateMessageReceived(messageIds: List<UUID>, status: MessageStatus = MessageStatus.DELIVERED)

    @Modifying
    @Query("UPDATE Message m SET m.status = :status WHERE m.id = :id")
    fun updateMessageReceived(id: UUID, status: MessageStatus = MessageStatus.DELIVERED)

    @Modifying
    @Query("UPDATE Message m SET m.status = :status WHERE m.id in :messageIds")
    fun updateMessageSeen(messageIds: List<UUID>, status: MessageStatus = MessageStatus.SEEN)
}