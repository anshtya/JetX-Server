package com.anshtya.jetx.server.userprofile.dto

import com.anshtya.jetx.server.util.USERNAME_REGEX
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CheckUsernameDto(
    @field:Size(max = 30, message = "Username must be of max 30 characters")
    @field:Pattern(regexp = USERNAME_REGEX, message = "Username can only contain letters, numbers, and underscores")
    val username: String
)
