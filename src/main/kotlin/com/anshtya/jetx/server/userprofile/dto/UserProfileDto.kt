package com.anshtya.jetx.server.userprofile.dto

import com.anshtya.jetx.server.userprofile.entity.UserProfile
import java.time.LocalDateTime

data class UserProfileDto(
    val username: String,
    val displayName: String,
    val phoneNumber: String?,
    val profilePhotoUrl: String?,
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
        profilePhotoUrl = profilePhotoUrl,
        isOnline = isOnline,
        lastSeen = lastSeen
    )
}
