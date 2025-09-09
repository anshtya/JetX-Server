package com.anshtya.jetx.server.userprofile.dto

import com.anshtya.jetx.server.userprofile.entity.UserProfile
import java.time.LocalDateTime

data class UserProfileDto(
    val username: String,
    val displayName: String,
    val profilePhotoUrl: String?,
    val isOnline: Boolean,
    val lastSeen: LocalDateTime?
)

fun UserProfile.toDto(): UserProfileDto {
    return UserProfileDto(
        username = username,
        displayName = displayName,
        profilePhotoUrl = profilePhotoUrl,
        isOnline = isOnline,
        lastSeen = lastSeen
    )
}
