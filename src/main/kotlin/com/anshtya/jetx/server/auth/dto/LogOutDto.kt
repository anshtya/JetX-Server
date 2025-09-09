package com.anshtya.jetx.server.auth.dto

import jakarta.validation.constraints.NotBlank

data class LogOutDto(
    @field:NotBlank(message = "Token is required")
    val token: String
)
