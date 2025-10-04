package com.anshtya.jetx.server.auth.dto

import java.util.UUID

data class AuthResultDto(
    val userId: UUID,
    val accessToken: String,
    val refreshToken: String
)
