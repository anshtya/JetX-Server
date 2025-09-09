package com.anshtya.jetx.server.auth.dto

data class AuthResultDto(
    val accessToken: String,
    val refreshToken: String
)
