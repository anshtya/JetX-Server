package com.anshtya.jetx.server.userprofile.dto

import com.anshtya.jetx.server.userprofile.entity.UserProfile
import java.time.LocalDateTime

data class UserProfileDto(
    val username: String,
    val displayName: String,
    val phoneNumber: String?,
    val photoExists: Boolean,
    val isOnline: Boolean,
    val lastSeen: LocalDateTime?
)

fun UserProfile.toDto(
    phoneNumber: String? = null
): UserProfileDto {
    return UserProfileDto(
        username = username,
        displayName = displayName,
        phoneNumber = phoneNumber,
        photoExists = photoExists,
        isOnline = isOnline,
        lastSeen = lastSeen
    )
}
