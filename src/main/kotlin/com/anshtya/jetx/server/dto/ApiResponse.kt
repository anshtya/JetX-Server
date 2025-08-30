package com.anshtya.jetx.server.dto

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
    val errors: List<String>? = null,
    val timestamp: Long = System.currentTimeMillis()
)