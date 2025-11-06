package com.anshtya.jetx.server.queue.entity

import com.anshtya.jetx.server.messaging.entity.Message
import com.anshtya.jetx.server.userprofile.entity.UserProfile
import jakarta.persistence.*

@Entity
@Table(
    name = "message_queue",
    indexes = [
        Index(
            name = "idx_userid",
            columnList = "user_id"
        )
    ]
)
data class MessageQueue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    val userProfile: UserProfile,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "message_id",
        nullable = false,
        updatable = false
    )
    val message: Message
)
