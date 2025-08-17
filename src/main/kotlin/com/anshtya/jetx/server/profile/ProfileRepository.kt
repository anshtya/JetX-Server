package com.anshtya.jetx.server.profile

import com.anshtya.jetx.server.database.entity.ProfileEntity
import com.anshtya.jetx.server.database.entity.ProfileTable
import com.anshtya.jetx.server.database.suspendTransaction

interface ProfileRepository {
    suspend fun createProfile(profile: Profile)

    suspend fun getProfile(username: String): Profile?
}

class ProfileRepositoryImpl : ProfileRepository {
    override suspend fun createProfile(profile: Profile) {
        suspendTransaction {
            ProfileEntity.new {
                username = profile.username
                name = profile.name
                bio = profile.bio
            }
        }
    }

    override suspend fun getProfile(username: String): Profile? = suspendTransaction {
        ProfileEntity.find { ProfileTable.username eq username }.firstOrNull()?.toModel()
    }
}