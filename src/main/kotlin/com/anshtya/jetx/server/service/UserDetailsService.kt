package com.anshtya.jetx.server.service

import com.anshtya.jetx.server.repository.UserRepository
import com.anshtya.jetx.server.security.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")
        return UserPrincipal(user)
    }

    fun loadUserByUserId(userId: Long): UserDetails {
        val user = userRepository.findById(userId).orElseThrow {
            UsernameNotFoundException("User not found: $userId")
        }
        return UserPrincipal(user)
    }
}