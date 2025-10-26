package com.anshtya.jetx.server.messaging.repository

import com.anshtya.jetx.server.messaging.entity.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface GroupRepository: JpaRepository<Group, UUID> {
    @Modifying
    @Query("UPDATE Group g SET g.description = :description WHERE g.id = :id")
    fun updateDescription(description: String, id: UUID)

    @Query("SELECT m.fcmToken FROM Group g JOIN g.members m WHERE g.id = :id")
    fun getMemberTokens(id: UUID): List<String>
}