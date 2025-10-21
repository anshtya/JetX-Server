package com.anshtya.jetx.server.messaging.dto

data class CreateGroupDto(
    val name: String,
    val photoExists: Boolean,
    val description: String?
)
