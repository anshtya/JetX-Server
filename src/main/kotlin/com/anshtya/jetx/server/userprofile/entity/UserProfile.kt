package com.anshtya.jetx.server.userprofile.entity

import com.anshtya.jetx.server.auth.entity.AuthUser
import com.anshtya.jetx.server.messaging.entity.Group
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.*

@Entity
@Table(name = "user_profile")
data class UserProfile(
    @Id
    val id: UUID? = null,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    val user: AuthUser,

    @Column(unique = true, nullable = false)
    var username: String,

    @Column(nullable = false)
    var displayName: String,

    @Column(nullable = false)
    var photoExists: Boolean = false,

    @Column
    var fcmToken: String? = null,

    @Column
    var lastSeen: Instant? = null,

    @Column
    var isOnline: Boolean = false,

    @CreationTimestamp
    @Column(
        nullable = false,
        updatable = false
    )
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: Instant = Instant.now(),

    @ManyToMany(
        mappedBy = "members",
        fetch = FetchType.LAZY
    )
    val groups: MutableSet<Group> = mutableSetOf()
)