package com.anshtya.jetx.server.response

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(
    val code: Int,
    val message: T
)