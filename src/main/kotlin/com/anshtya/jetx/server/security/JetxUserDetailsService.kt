package com.anshtya.jetx.server.security

import com.anshtya.jetx.server.auth.repository.AuthUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class JetxUserDetailsService(
    private val authUserRepository: AuthUserRepository
) : UserDetailsService {
    // Anything which uniquely identifies user, here it is phone number
    override fun loadUserByUsername(phoneNumber: String): UserDetails {
        val user = authUserRepository.findByPhoneNumber(phoneNumber)
            ?: throw UsernameNotFoundException("User not found: $phoneNumber")
        return user
    }

    fun loadUserByUserId(userId: UUID): UserDetails {
        val user = authUserRepository.findById(userId).orElseThrow {
            UsernameNotFoundException("User not found: $userId")
        }
        return user
    }
}