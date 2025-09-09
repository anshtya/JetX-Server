package com.anshtya.jetx.server.userprofile.entity

import com.anshtya.jetx.server.auth.entity.AuthUser
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_profile")
class UserProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: AuthUser,

    @Column(unique = true, nullable = false)
    var username: String,

    @Column(nullable = false)
    var displayName: String,

    @Column
    var profilePhotoUrl: String? = null,

    @Column
    var fcmToken: String? = null,

    @Column
    var lastSeen: LocalDateTime? = null,

    @Column
    var isOnline: Boolean = false,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)