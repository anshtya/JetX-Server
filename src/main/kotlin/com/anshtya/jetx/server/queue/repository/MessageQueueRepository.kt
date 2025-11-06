package com.anshtya.jetx.server.queue.repository

import com.anshtya.jetx.server.queue.entity.MessageQueue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface MessageQueueRepository: JpaRepository<MessageQueue, Long> {
    @Query("SELECT * FROM message_queue WHERE user_id = :userId", nativeQuery = true)
    fun findByUserId(userId: UUID): List<MessageQueue>

    @Modifying
    @Query("INSERT INTO message_queue (user_id, message_id) VALUES (:userId, :messageId)", nativeQuery = true)
    fun saveFromId(userId: UUID, messageId: UUID)

    @Modifying
    @Query("INSERT INTO message_queue (user_id, message_id) VALUES (:userIds, :messageId)", nativeQuery = true)
    fun saveFromIds(userIds: List<UUID>, messageId: UUID)
}