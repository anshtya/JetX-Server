package com.anshtya.jetx.server.result

import kotlinx.serialization.Serializable

@Serializable
data class RequestResult<T>(
    val code: Int,
    val message: T
)