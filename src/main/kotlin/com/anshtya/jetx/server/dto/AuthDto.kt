package com.anshtya.jetx.server.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    val phoneNumber: String,

    @field:NotBlank(message = "PIN is required")
    @field:Size(min = 4, max = 6, message = "PIN must be 4-6 digits")
    @field:Pattern(regexp = "^\\d+$", message = "PIN must contain only digits")
    val pin: String,

    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    val username: String,

    @field:NotBlank(message = "Display name is required")
    @field:Size(min = 1, max = 50, message = "Display name must be 1-50 characters")
    val displayName: String,

    val fcmToken: String? = null
)

data class LoginRequest(
    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String,

    @field:NotBlank(message = "PIN is required")
    val pin: String,

    val fcmToken: String? = null
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)
