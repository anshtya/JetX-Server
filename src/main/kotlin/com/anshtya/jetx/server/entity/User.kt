package com.anshtya.jetx.server.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val phoneNumber: String,

    @Column(nullable = false)
    val pinHash: String,

    @Column(unique = true, nullable = false)
    val username: String,

    @Column(nullable = false)
    val displayName: String,

    @Column
    val profilePhotoUrl: String? = null,

    @Column
    val fcmToken: String? = null,

    @Column
    val lastSeen: LocalDateTime? = null,

    @Column
    val isOnline: Boolean = false,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    private constructor(): this(phoneNumber = "", pinHash = "", username = "", displayName = "")
}