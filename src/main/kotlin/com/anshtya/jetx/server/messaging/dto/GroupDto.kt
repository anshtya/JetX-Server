package com.anshtya.jetx.server.messaging.dto

import com.anshtya.jetx.server.messaging.entity.Group
import java.util.*

data class GroupDto(
    val id: UUID,
    val photoExists: Boolean,
    val description: String?
)

fun Group.toDto(): GroupDto {
   return GroupDto(
       id = id!!,
       photoExists = false,
       description = description
   )
}