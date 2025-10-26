package com.anshtya.jetx.server.userprofile.dto

import com.anshtya.jetx.server.userprofile.entity.UserProfile
import java.util.*

data class UserProfileSearchDto(
    val users: List<UserProfileSearchItem>
)

data class UserProfileSearchItem(
    val id: UUID,
    val username: String,
    val displayName: String
)

fun UserProfile.toSearchResultDto(): UserProfileSearchItem {
    return UserProfileSearchItem(
        id = id!!,
        username = username,
        displayName = displayName
    )
}