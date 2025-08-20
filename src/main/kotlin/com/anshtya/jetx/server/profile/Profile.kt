package com.anshtya.jetx.server.profile

import com.anshtya.jetx.server.database.entity.ProfileEntity
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val username: String,
    val name: String,
    val bio: String? = null
)

fun ProfileEntity.toModel(): Profile {
    return Profile(
        username = username,
        name = name,
        bio = bio
    )
}