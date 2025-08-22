package com.anshtya.jetx.server.profile

import com.anshtya.jetx.server.database.entity.ProfileEntity
import com.anshtya.jetx.server.database.entity.ProfileTable
import com.anshtya.jetx.server.database.suspendTransaction

class ProfileService {
    suspend fun createProfile(profile: Profile) {
        suspendTransaction {
            ProfileEntity.new {
                username = profile.username
                name = profile.name
                bio = profile.bio
            }
        }
    }

    suspend fun getProfile(username: String): Profile? = suspendTransaction {
        ProfileEntity.find { ProfileTable.username eq username }.firstOrNull()?.toModel()
    }
}