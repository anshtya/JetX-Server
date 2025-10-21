package com.anshtya.jetx.server.messaging.entity

import com.anshtya.jetx.server.userprofile.entity.UserProfile
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "message",
    indexes = [
        Index(
            name = "idx_individual_message",
            columnList = "sender_id, receiver_id, created_at"
        ),
        Index(
            name = "idx_group_message",
            columnList = "group_id, created_at"
        )
    ]
)
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, updatable = false)
    val type: MessageType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sender_id",
        nullable = false,
        updatable = false
    )
    val sender: UserProfile,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "receiver_id",
        updatable = false
    )
    val receiver: UserProfile? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "group_id",
        updatable = false
    )
    val group: Group? = null,

    @Column
    val content: String?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "attachment_id",
        updatable = false
    )
    val attachment: Attachment? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: MessageStatus = MessageStatus.SENT,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var isDeleted: Boolean = false
) {
    companion object {
        fun createIndividualMessage(
            sender: UserProfile,
            receiver: UserProfile,
            content: String? = null,
            attachment: Attachment? = null
        ): Message {
            return Message(
                type = MessageType.INDIVIDUAL,
                sender = sender,
                receiver = receiver,
                content = content,
                attachment = attachment
            )
        }

        fun createGroupMessage(
            sender: UserProfile,
            group: Group,
            content: String? = null,
            attachment: Attachment? = null
        ): Message {
            return Message(
                type = MessageType.GROUP,
                sender = sender,
                group = group,
                content = content,
                attachment = attachment
            )
        }
    }
}

enum class MessageType {
    INDIVIDUAL, GROUP
}

enum class MessageStatus {
    SENT, DELIVERED, SEEN
}