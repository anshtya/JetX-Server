package com.anshtya.jetx.server.messaging.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "attachment")
data class Attachment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val name: String
)