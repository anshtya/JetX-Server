package com.anshtya.jetx.server.messaging.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import java.util.*

@Entity
@Table(name = "attachment")
data class Attachment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String,

    @Column(updatable = false)
    val width: Int,

    @Column(updatable = false)
    val height: Int,

    @Column(
        nullable = false,
        updatable = false
    )
    val type: String,

    @CreationTimestamp
    @Column(
        nullable = false,
        updatable = false
    )
    val createdAt: Instant = Instant.now()
)