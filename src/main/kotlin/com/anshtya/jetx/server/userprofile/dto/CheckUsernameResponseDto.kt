package com.anshtya.jetx.server.userprofile.dto

data class CheckUsernameResponseDto(
    val valid: Boolean,
    val message: String?
)