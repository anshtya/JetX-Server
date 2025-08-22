package com.anshtya.jetx.server.profile.di

import com.anshtya.jetx.server.profile.ProfileService
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ProfileModule {
    @Single
    fun provideProfileService(): ProfileService = ProfileService()
}