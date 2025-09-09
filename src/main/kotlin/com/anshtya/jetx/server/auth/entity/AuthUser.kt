package com.anshtya.jetx.server.auth.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "auth_user")
class AuthUser(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(unique = true, nullable = false)
    val phoneNumber: String,

    @Column(nullable = false)
    val pinHash: String,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf()
    }

    override fun getPassword(): String {
        return pinHash
    }

    override fun getUsername(): String {
        return phoneNumber
    }
}