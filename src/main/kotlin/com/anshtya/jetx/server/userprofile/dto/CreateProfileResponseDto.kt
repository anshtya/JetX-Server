package com.anshtya.jetx.server.userprofile.dto

import com.anshtya.jetx.server.userprofile.entity.UserProfile

data class CreateProfileResponseDto(
    val username: String,
    val displayName: String,
    val phoneNumber: String
)

fun UserProfile.toCreateResponseDto(
    phoneNumber: String,
): CreateProfileResponseDto {
    return CreateProfileResponseDto(
        username = username,
        displayName = displayName,
        phoneNumber = phoneNumber
    )
}
