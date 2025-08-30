package com.anshtya.jetx.server.dto

import com.anshtya.jetx.server.entity.User
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val username: String,
    val displayName: String,
    val profilePhotoUrl: String?,
    val isOnline: Boolean,
    val lastSeen: LocalDateTime?
)

data class UpdateProfileRequest(
    @field:Size(min = 1, max = 50, message = "Display name must be 1-50 characters")
    val displayName: String?,

    @field:Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    val username: String?
)

data class UserSearchResponse(
    val users: List<UserDto>
)

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        username = this.username,
        displayName = this.displayName,
        profilePhotoUrl = this.profilePhotoUrl,
        isOnline = this.isOnline,
        lastSeen = this.lastSeen
    )
}