package com.anshtya.jetx.server.auth.repository

import com.anshtya.jetx.server.auth.entity.AuthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthUserRepository: JpaRepository<AuthUser, UUID> {
    fun findByPhoneNumber(phoneNumber: String): AuthUser?
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}