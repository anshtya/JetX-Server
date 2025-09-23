package com.anshtya.jetx.server.auth.dto

import com.anshtya.jetx.server.util.PHONE_NUMBER_REGEX
import com.anshtya.jetx.server.util.PIN_REGEX
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class LogInDto(
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(regexp = PHONE_NUMBER_REGEX, message = "Invalid phone number format")
    val phoneNumber: String,

    @field:NotBlank(message = "PIN is required")
    @field:Size(min = 4, max = 4, message = "PIN must be of 4 digits")
    @field:Pattern(regexp = PIN_REGEX, message = "PIN must contain only digits")
    val pin: String,

    @field:NotBlank(message = "FCM token is required")
    val fcmToken: String
)
