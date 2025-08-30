package com.anshtya.jetx.server.repository

import com.anshtya.jetx.server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByPhoneNumber(phoneNumber: String): User?
    fun findByUsername(username: String): User?
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun existsByUsername(username: String): Boolean

    @Query("SELECT u FROM User u WHERE u.username LIKE %:query% OR u.displayName LIKE %:query%")
    fun searchUsers(query: String): List<User>

    @Modifying
    @Query("UPDATE User u SET u.isOnline = :status, u.lastSeen = :lastSeen WHERE u.id = :userId")
    fun updateUserPresence(userId: Long, status: Boolean, lastSeen: LocalDateTime)

    @Modifying
    @Query("UPDATE User u SET u.fcmToken = :token WHERE u.id = :userId")
    fun updateFcmToken(userId: Long, token: String)
}