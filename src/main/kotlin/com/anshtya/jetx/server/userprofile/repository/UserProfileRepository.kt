package com.anshtya.jetx.server.userprofile.repository

import com.anshtya.jetx.server.messaging.entity.Group
import com.anshtya.jetx.server.userprofile.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, UUID> {
    fun findByUsername(username: String): UserProfile?
    fun existsByUsername(username: String): Boolean

    @Query("SELECT u FROM UserProfile u WHERE u.username LIKE %:query% OR u.displayName LIKE %:query%")
    fun searchUsers(query: String): List<UserProfile>

    @Modifying
    @Query("UPDATE UserProfile u SET u.isOnline = :status, u.lastSeen = :lastSeen WHERE u.id = :id")
    fun updateUserPresence(id: UUID, status: Boolean, lastSeen: Instant)

    @Modifying
    @Query("UPDATE UserProfile u SET u.fcmToken = :token WHERE u.id = :id")
    fun updateFcmToken(id: UUID, token: String?)

    @Query("SELECT g FROM Group g JOIN g.members u WHERE u.id = :userId")
    fun findGroupsByUserProfileId(userId: UUID): List<Group>
}