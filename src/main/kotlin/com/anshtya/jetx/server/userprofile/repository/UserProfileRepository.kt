package com.anshtya.jetx.server.userprofile.repository

import com.anshtya.jetx.server.userprofile.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, UUID> {
    fun findByUsername(username: String): UserProfile?
    fun findByUserId(userId: UUID): UserProfile?
    fun existsByUsername(username: String): Boolean

    @Query("SELECT u FROM UserProfile u WHERE u.username LIKE %:query% OR u.displayName LIKE %:query%")
    fun searchUsers(query: String): List<UserProfile>

    @Modifying
    @Query("UPDATE UserProfile u SET u.isOnline = :status, u.lastSeen = :lastSeen WHERE u.user.id = :userId")
    fun updateUserPresence(userId: UUID, status: Boolean, lastSeen: LocalDateTime)

    @Modifying
    @Query("UPDATE UserProfile u SET u.fcmToken = :token WHERE u.user.id = :userId")
    fun updateFcmToken(userId: UUID, token: String)
}