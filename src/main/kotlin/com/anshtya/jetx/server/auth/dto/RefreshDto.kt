package com.anshtya.jetx.server.auth.dto

import jakarta.validation.constraints.NotBlank

data class RefreshDto(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)
