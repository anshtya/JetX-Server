package com.anshtya.jetx.server.userprofile.dto

import com.anshtya.jetx.server.util.USERNAME_REGEX
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateProfileDto(
    @field:Size(min = 1, max = 50, message = "Display name must be 1-50 characters")
    val displayName: String,

    @field:Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    @field:Pattern(regexp = USERNAME_REGEX, message = "Username can only contain letters, numbers, and underscores")
    val username: String,

    val fcmToken: String
)
