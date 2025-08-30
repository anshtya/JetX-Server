package com.anshtya.jetx.server.security

import com.anshtya.jetx.server.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    private val user: User
) : UserDetails {

    fun getId(): Long = user.id

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getPassword(): String = user.pinHash

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun getUser(): User = user
}