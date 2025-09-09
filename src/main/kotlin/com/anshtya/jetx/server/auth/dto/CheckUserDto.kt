package com.anshtya.jetx.server.auth.dto

import com.anshtya.jetx.server.util.PHONE_NUMBER_REGEX
import jakarta.validation.constraints.Pattern

data class CheckUserDto(
    @field:Pattern(regexp = PHONE_NUMBER_REGEX, message = "Invalid phone number format")
    val phoneNumber: String
)
